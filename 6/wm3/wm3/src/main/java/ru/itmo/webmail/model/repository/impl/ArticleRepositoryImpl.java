package ru.itmo.webmail.model.repository.impl;

import ru.itmo.webmail.model.database.DatabaseUtils;
import ru.itmo.webmail.model.domain.Article;
import ru.itmo.webmail.model.exception.RepositoryException;
import ru.itmo.webmail.model.repository.ArticleRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticleRepositoryImpl implements ArticleRepository {
    private static final DataSource DATA_SOURCE = DatabaseUtils.getDataSource();

    @Override
    public void save(long userId, String title, String textArticle) {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO `Article` (`userId`, `title`, `text`, `creationTime`) VALUES (?, ?, ?, NOW());",
                    Statement.RETURN_GENERATED_KEYS)) {
                statement.setLong(1, userId);
                statement.setString(2, title);
                statement.setString(3, textArticle);
                if (statement.executeUpdate() == 1) {
                    ResultSet generatedIdResultSet = statement.getGeneratedKeys();
                    if (!generatedIdResultSet.next()) {
                        throw new RepositoryException("Can't find id of saved Article.");
                    }
                } else {
                    throw new RepositoryException("Can't save Article.");
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't save Article.", e);
        }
    }

    @Override
    public List<Article> findAll() {
        List<Article> articles = new ArrayList<>();
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM `Article` WHERE `hidden` = 'false' ORDER BY `creationTime` DESC")) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        articles.add(toArticle(statement.getMetaData(), resultSet));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find all articles.", e);
        }
        return articles;
    }

    @Override
    public List<Article> findAll(long userId) {
        List<Article> articles = new ArrayList<>();
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM `Article` WHERE `userId` = ? ORDER BY `creationTime` DESC")) {
                statement.setLong(1, userId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        articles.add(toArticle(statement.getMetaData(), resultSet));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find all articles from userId.", e);
        }
        return articles;
    }

    @Override
    public void update(long articalId, long type) {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "UPDATE `Article` SET `hidden` = ? WHERE `Article`.`id` = ?;")) {
                statement.setString(1, ((type == 1) ? "false" : "true"));
                statement.setLong(2, articalId);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't update hidden value.", e);
        }
    }

        private Article toArticle (ResultSetMetaData metaData, ResultSet resultSet) throws SQLException {
            Article article = new Article();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                String columnName = metaData.getColumnName(i);
                if ("id".equalsIgnoreCase(columnName)) {
                    article.setId(resultSet.getLong(i));
                } else if ("userId".equalsIgnoreCase(columnName)) {
                    article.setUserId(resultSet.getLong(i));
                } else if ("title".equalsIgnoreCase(columnName)) {
                    article.setTitle(resultSet.getString(i));
                } else if ("text".equalsIgnoreCase(columnName)) {
                    article.setText(resultSet.getString(i));
                } else if ("hidden".equalsIgnoreCase(columnName)) {
                    article.setHidden(resultSet.getString(i));
                } else if ("creationTime".equalsIgnoreCase(columnName)) {
                    article.setCreationTime(resultSet.getTimestamp(i));
                } else {
                    throw new RepositoryException("Unexpected column 'User." + columnName + "'.");
                }
            }
            return article;
        }
    }
