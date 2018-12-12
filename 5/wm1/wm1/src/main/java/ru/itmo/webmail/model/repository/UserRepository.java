package ru.itmo.webmail.model.repository;

import ru.itmo.webmail.model.domain.Message;
import ru.itmo.webmail.model.domain.User;

import java.sql.SQLException;
import java.util.List;

public interface UserRepository {
    User find(long userId);
    User findByLogin(String login);
    User findByEmail(String email);
    User findByLoginOrEmailAndPasswordSha(String login, String passwordSha);
    User findByLoginOrEmail(String login, String email);
    List<User> findAll();
    void save(User user, String passwordSha);
}
