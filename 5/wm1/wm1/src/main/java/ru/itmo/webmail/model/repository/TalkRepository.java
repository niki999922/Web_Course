package ru.itmo.webmail.model.repository;

import ru.itmo.webmail.model.domain.Message;
import ru.itmo.webmail.model.domain.User;

import java.sql.SQLException;
import java.util.List;

public interface TalkRepository {
    List<Message> findAllMessage(String firstPerson, String secondPerson) throws SQLException;
    List<Message> findAllMessage(String firstPerson, List<User> userList) throws SQLException;
    void addMessage(long fromUser, long toUser, String message) throws SQLException;

}
