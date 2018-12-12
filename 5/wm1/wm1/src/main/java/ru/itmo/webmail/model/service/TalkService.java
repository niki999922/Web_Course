package ru.itmo.webmail.model.service;

import ru.itmo.webmail.model.domain.Message;
import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.exception.RepositoryException;
import ru.itmo.webmail.model.exception.ValidationException;
import ru.itmo.webmail.model.repository.TalkRepository;
import ru.itmo.webmail.model.repository.UserRepository;
import ru.itmo.webmail.model.repository.impl.TalkRepositoryImpl;
import ru.itmo.webmail.model.repository.impl.UserRepositoryImpl;

import java.sql.SQLException;
import java.util.List;


@SuppressWarnings("UnstableApiUsage")
public class TalkService {
    private TalkRepository talkRepository = new TalkRepositoryImpl();
    private UserRepository userRepository = new UserRepositoryImpl();

    public List<Message> findAllMessage(String sourceUser, String targetUser) throws ValidationException, SQLException {
        return talkRepository.findAllMessage(sourceUser, targetUser);
    }

    public List<Message> findAllMessage(String sourceUser, List<User> targetUsers) throws ValidationException, SQLException {
        return talkRepository.findAllMessage(sourceUser, targetUsers);
    }

    public void sendMessage(long fromUser, String toUser, String message) throws ValidationException, SQLException {
        UserService.checkLogin(toUser);
        User user = userRepository.findByLogin(toUser);
        if (user == null) throw new RepositoryException("This user does not real");
        talkRepository.addMessage(fromUser, user.getId() , message);
    }
}
