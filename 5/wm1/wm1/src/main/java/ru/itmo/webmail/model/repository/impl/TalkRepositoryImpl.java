package ru.itmo.webmail.model.repository.impl;

import javafx.util.Pair;
import ru.itmo.webmail.model.database.DatabaseUtils;
import ru.itmo.webmail.model.domain.Message;
import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.exception.RepositoryException;
import ru.itmo.webmail.model.repository.TalkRepository;
import ru.itmo.webmail.model.repository.UserRepository;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TalkRepositoryImpl implements TalkRepository {
    private static final UserRepository userRepository = new UserRepositoryImpl();

    @Override
    public List<Message> findAllMessage(String firstPerson, String secondPerson) throws SQLException {
        User firstUser = userRepository.findByLogin(firstPerson);
        User secondUser = userRepository.findByLogin(secondPerson);
        if (firstUser == null || secondUser == null) throw new RepositoryException("This user does not real");
        Pair<ResultSet, ResultSetMetaData> result = DatabaseUtils.process("Can't find all users messages.","SELECT *  FROM `Talk` WHERE ((`Talk`.`sourceUserId`=? AND `Talk`.`targetUserId`=?) OR (`Talk`.`sourceUserId`=? AND `Talk`.`targetUserId`=?)) ORDER BY `creationTime`;", String.valueOf(firstUser.getId()), String.valueOf(secondUser.getId()), String.valueOf(secondUser.getId()), String.valueOf(firstUser.getId()));
        List<Message> messages = new ArrayList<>();
        while (result.getKey().next()) {
            messages.add(toMessage(result.getValue(), result.getKey()));
        }
        for (Message message : messages) {
            message.setSourceUser((Long.parseLong(message.getSourceUser()) == firstUser.getId()) ? firstPerson : secondPerson);
            message.setTargetUser((Long.parseLong(message.getTargetUser()) == secondUser.getId()) ? secondPerson : firstPerson);
        }
        return messages;
    }

    @Override
    public List<Message> findAllMessage(String firstPerson, List<User> userList) throws SQLException {
        User firstUser = userRepository.findByLogin(firstPerson);
        if (firstUser == null) throw new RepositoryException("This user does not real");
        Pair<ResultSet, ResultSetMetaData> result = DatabaseUtils.process("Can't find all users messages.","SELECT * FROM `Talk` WHERE ((`Talk`.`targetUserId` = ?) OR (`Talk`.`sourceUserId` = ?)) ORDER BY `creationTime` ASC;", String.valueOf(firstUser.getId()), String.valueOf(firstUser.getId()));
        List<Message> messages = new ArrayList<>();
        User tmp = null;
        while (result.getKey().next()) {
            messages.add(toMessage(result.getValue(), result.getKey()));
        }
        for (Message message : messages) {
            if (Long.parseLong(message.getSourceUser()) == firstUser.getId()) {
                for (User user : userList) {
                    if (user.getId() == Long.parseLong(message.getTargetUser())) {
                        tmp = user;       
                        break;
                    }
                }
                message.setSourceUser(firstPerson);
                assert tmp != null;
                message.setTargetUser(tmp.getLogin());
            } else {
                for (User user : userList) {
                    if (user.getId() == Long.parseLong(message.getSourceUser())) {
                        tmp = user;
                        break;
                    }
                }
                assert tmp != null;
                message.setSourceUser(tmp.getLogin());
                message.setTargetUser(firstPerson);
            }
        }
        return messages;
    }

    @Override
    public void addMessage(long fromUser, long toUser, String message) throws SQLException {
        DatabaseUtils.process("Can't save Message.", "INSERT INTO `Talk` (`sourceUserId`, `targetUserId`, `text`, `creationTime`) VALUES (?, ?, ?, NOW());", DatabaseUtils.QueryType.INSERT, String.valueOf(fromUser), String.valueOf(toUser), message);
    }

    private static Message toMessage(ResultSetMetaData metaData, ResultSet resultSet) throws SQLException {
        Message message = new Message();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            String columnName = metaData.getColumnName(i);
            if ("id".equalsIgnoreCase(columnName)) {
                message.setId(resultSet.getLong(i));
            } else if ("sourceUserId".equalsIgnoreCase(columnName)) {
                message.setSourceUser(resultSet.getString(i));
            } else if ("targetUserId".equalsIgnoreCase(columnName)) {
                message.setTargetUser(resultSet.getString(i));
            } else if ("text".equalsIgnoreCase(columnName)) {
                message.setTextMessage(resultSet.getString(i));
            } else if ("creationTime".equalsIgnoreCase(columnName)) {
                message.setCreationTime(resultSet.getTimestamp(i));
            } else {
                throw new RepositoryException("Unexpected column 'User." + columnName + "'.");
            }
        }
        return message;
    }
    
}
