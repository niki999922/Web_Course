package ru.itmo.webmail.model.repository;

import ru.itmo.webmail.model.domain.Message;
import ru.itmo.webmail.model.domain.User;

import java.sql.SQLException;
import java.util.List;

public interface EmailConfirmationRepository {
    void confirm(String secret) throws SQLException;
    void addConfirm(User user, String secret);
    void correctConfirmUser(long userId);
}
