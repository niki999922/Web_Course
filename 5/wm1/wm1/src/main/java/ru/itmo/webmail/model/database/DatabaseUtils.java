package ru.itmo.webmail.model.database;

import javafx.util.Pair;
import org.mariadb.jdbc.MariaDbDataSource;
import ru.itmo.webmail.model.exception.RepositoryException;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.sql.*;

public class DatabaseUtils {
    public enum QueryType {
        FIND, INSERT
    }

    public static DataSource getDataSource() {
        return DataSourceHolder.INSTANCE;
    }

    private static Connection getConnection() throws SQLException {
        return DataSourceHolder.INSTANCE.getConnection();
    }

    public static PreparedStatement getInsertStatement(String exceptionMessage, String requestStatement) {
        try {
            return getConnection().prepareStatement(requestStatement, Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException e) {
            throw new RepositoryException(exceptionMessage, e);
        }
    }

    private static PreparedStatement getStatement(String exceptionMessage, String requestStatement) {
        try {
            return getConnection().prepareStatement(requestStatement);
        } catch (SQLException e) {
            throw new RepositoryException(exceptionMessage, e);
        }
    }

    public static ResultSet getResultSet(PreparedStatement statement, String exceptionMessage) {
        try {
            return statement.executeQuery();
        } catch (SQLException e) {
            throw new RepositoryException(exceptionMessage, e);
        }
    }

    public static ResultSet process(String errorMessage, String query, QueryType type, String... params) {
        PreparedStatement statement = DatabaseUtils.getStatement(errorMessage, query);
        try {
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            switch (type) {
                case FIND:
                    return statement.executeQuery();
                case INSERT:
                    if (statement.executeUpdate() != 1)
                        throw new RepositoryException(errorMessage);
                    return null;
                default:
                    return null;
            }
        } catch (SQLException e) {
            throw new RepositoryException(errorMessage, e);
        }
    }

    public static Pair<ResultSet, ResultSetMetaData> process(String errorMessage, String query, String... params) {
        PreparedStatement statement = DatabaseUtils.getStatement(errorMessage, query);
        try {
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            return new Pair<>(statement.executeQuery(), statement.getMetaData());
        } catch (SQLException e) {
            throw new RepositoryException(errorMessage, e);
        }
    }

    private static final class DataSourceHolder {
        private static final DataSource INSTANCE;
        private static final Properties PROPERTIES = new Properties();

        static {
            try {
                PROPERTIES.load(DataSourceHolder.class.getResourceAsStream("/application.properties"));
            } catch (IOException e) {
                throw new RuntimeException("Can't load application.properties.", e);
            }

            try {
                MariaDbDataSource dataSource = new MariaDbDataSource();
                dataSource.setUrl(PROPERTIES.getProperty("database.url"));
                dataSource.setUser(PROPERTIES.getProperty("database.user"));
                dataSource.setPassword(PROPERTIES.getProperty("database.password"));
                INSTANCE = dataSource;
            } catch (SQLException e) {
                throw new RuntimeException("Can't initialize DB.", e);
            }

            try (Connection connection = INSTANCE.getConnection()) {
                if (connection == null) {
                    throw new RuntimeException("Can't get testing connection from DB.");
                }
            } catch (SQLException e) {
                throw new RuntimeException("Can't get testing connection from DB.", e);
            }
        }
    }
}
