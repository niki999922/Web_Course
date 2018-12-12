package ru.itmo.webmail.model.repository.impl;

import javafx.util.Pair;
import ru.itmo.webmail.model.database.DatabaseUtils;
import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.exception.RepositoryException;
import ru.itmo.webmail.model.repository.UserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {
    @Override
    public User find(long userId) {
        Pair<ResultSet, ResultSetMetaData> result = DatabaseUtils.process("Can't find User by id.", "SELECT * FROM User WHERE id=?;", String.valueOf(userId));
        try {
            if ((result.getKey().next())) {
                return toUser(result.getValue(), result.getKey());
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find User by id.", e);
        }
    }

    @Override
    public User findByLogin(String login) {
        Pair<ResultSet, ResultSetMetaData> result = DatabaseUtils.process("Can't find User by login.", "SELECT * FROM User WHERE login=?;", login);
        try {
            if ((result.getKey().next())) {
                return toUser(result.getValue(), result.getKey());
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find User by login.", e);
        }
    }

    @Override
    public User findByEmail(String email) {
        Pair<ResultSet, ResultSetMetaData> result = DatabaseUtils.process("Can't find User by email.", "SELECT * FROM User WHERE email=?;", email);
        try {
            if ((result.getKey().next())) {
                return toUser(result.getValue(), result.getKey());
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find User by email.", e);
        }
    }

    @Override
    public User findByLoginOrEmail(String login, String email) {
        Pair<ResultSet, ResultSetMetaData> result = DatabaseUtils.process("Can't find User by id and passwordSha.", "SELECT * FROM User WHERE login=? OR email=?;", login, email);
        try {
            if ((result.getKey().next())) {
                return toUser(result.getValue(), result.getKey());
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find User by id and passwordSha.", e);
        }
    }

    @Override
    public User findByLoginOrEmailAndPasswordSha(String login, String passwordSha) {
        Pair<ResultSet, ResultSetMetaData> result = DatabaseUtils.process("Can't find User by id or Email and passwordSha.", "SELECT * FROM User WHERE (login=? OR email=?) AND passwordSha=?;", login, login, passwordSha);
        try {
            if ((result.getKey().next())) {
                return toUser(result.getValue(), result.getKey());
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find User by id or Email and passwordSha.", e);
        }
    }

    @Override
    public List<User> findAll() {
        Pair<ResultSet, ResultSetMetaData> result = DatabaseUtils.process("Can't find all users.", "SELECT * FROM User WHERE `confirmed` = 'true' ORDER BY id;");
        List<User> users = new ArrayList<>();
        try {
            while (result.getKey().next()) {
                users.add(toUser(result.getValue(), result.getKey()));
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find all users.", e);
        }
        return users;
    }

    @Override
    public void save(User user, String passwordSha) {
        PreparedStatement statement = DatabaseUtils.getInsertStatement("Can't save User.", "INSERT INTO User (login, email, passwordSha, creationTime) VALUES (?, ?, ?, NOW())");
        try {
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getEmail());
            statement.setString(3, passwordSha);
            if (statement.executeUpdate() == 1) {
                ResultSet generatedIdResultSet = statement.getGeneratedKeys();
                if (generatedIdResultSet.next()) {
                    user.setId(generatedIdResultSet.getLong(1));
                    user.setCreationTime(findCreationTime(user.getId()));
                } else {
                    throw new RepositoryException("Can't find id of saved User.");
                }
            } else {
                throw new RepositoryException("Can't save User.");
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't save User.", e);
        }
        //DatabaseUtils.process("Can't save User.", "INSERT INTO User (login, email, passwordSha, creationTime) VALUES (?, ?, ?, NOW());", DatabaseUtils.QueryType.INSERT, user.getLogin(), user.getEmail(), passwordSha);
        /*ResultSet generatedIdResultSet = statement.getGeneratedKeys();
        if (generatedIdResultSet.next()) {
            user.setId(generatedIdResultSet.getLong(1));
            user.setCreationTime(findCreationTime(user.getId()));
        } else {
            throw new RepositoryException("Can't find id of saved User.");
        }
        PreparedStatement statement = DatabaseUtils.getInsertStatement("Can't save User.", "INSERT INTO User (login, email, passwordSha, creationTime) VALUES (?, ?, ?, NOW())");
        try {
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getEmail());
            statement.setString(3, passwordSha);
            if (statement.executeUpdate() == 1) {
                ResultSet generatedIdResultSet = statement.getGeneratedKeys();
                if (generatedIdResultSet.next()) {
                    user.setId(generatedIdResultSet.getLong(1));
                    user.setCreationTime(findCreationTime(user.getId()));
                } else {
                    throw new RepositoryException("Can't find id of saved User.");
                }
            } else {
                throw new RepositoryException("Can't save User.");
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't save User.", e);
        }*/
    }

    private Date findCreationTime(long userId) {
        ResultSet resultSet = DatabaseUtils.process("Can't find User.creationTime by id.", "SELECT creationTime FROM User WHERE id=?;", DatabaseUtils.QueryType.FIND, String.valueOf(userId));
        try {
            if (resultSet.next()) {
                return resultSet.getTimestamp(1);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find User.creationTime by id.", e);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    private static User toUser(ResultSetMetaData metaData, ResultSet resultSet) throws SQLException {
        User user = new User();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            String columnName = metaData.getColumnName(i);
            if ("id".equalsIgnoreCase(columnName)) {
                user.setId(resultSet.getLong(i));
            } else if ("login".equalsIgnoreCase(columnName)) {
                user.setLogin(resultSet.getString(i));
            } else if ("email".equalsIgnoreCase(columnName)) {
                user.setEmail(resultSet.getString(i));
            } else if ("confirmed".equalsIgnoreCase(columnName)) {
                user.setConfirmed(resultSet.getString(i));
            } else if ("passwordSha".equalsIgnoreCase(columnName)) {
                // No operations.
            } else if ("creationTime".equalsIgnoreCase(columnName)) {
                user.setCreationTime(resultSet.getTimestamp(i));
            } else {
                throw new RepositoryException("Unexpected column 'User." + columnName + "'.");
            }
        }
        return user;
    }

}
