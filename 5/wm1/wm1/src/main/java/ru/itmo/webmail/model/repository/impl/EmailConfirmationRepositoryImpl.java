package ru.itmo.webmail.model.repository.impl;

import ru.itmo.webmail.model.database.DatabaseUtils;
import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.repository.EmailConfirmationRepository;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmailConfirmationRepositoryImpl implements EmailConfirmationRepository {
    @Override
    public void addConfirm(User user, String secret) {
        DatabaseUtils.process("Can't save EmailConfirmation.", "INSERT INTO `EmailConfirmation` (`userId`, `secret`, `creationTime`) VALUES (?, ?, NOW());", DatabaseUtils.QueryType.INSERT, String.valueOf(user.getId()), secret);
    }

    @Override
    public void confirm(String secret) throws SQLException {
        ResultSet resultSet = DatabaseUtils.process("Can't check Confirmation.", "SELECT * FROM `EmailConfirmation` WHERE `secret` LIKE ?;", DatabaseUtils.QueryType.FIND, secret);
        assert resultSet != null;
        if (resultSet.next()) {
            correctConfirmUser(resultSet.getLong(2));
        }
    }

    @Override
    public void correctConfirmUser(long userId) {
        DatabaseUtils.process("Can't save EmailConfirmation.", "UPDATE `User` SET `confirmed` = 'true' WHERE `User`.`id` = ?;", DatabaseUtils.QueryType.INSERT, String.valueOf(userId));
    }
}
