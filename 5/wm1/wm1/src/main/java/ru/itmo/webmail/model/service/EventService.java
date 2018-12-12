package ru.itmo.webmail.model.service;

import com.google.common.hash.Hashing;
import ru.itmo.webmail.model.domain.Message;
import ru.itmo.webmail.model.domain.User;
import ru.itmo.webmail.model.exception.RepositoryException;
import ru.itmo.webmail.model.exception.ValidationException;
import ru.itmo.webmail.model.repository.EventRepository;
import ru.itmo.webmail.model.repository.UserRepository;
import ru.itmo.webmail.model.repository.impl.EventRepositoryImpl;
import ru.itmo.webmail.model.repository.impl.UserRepositoryImpl;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.abs;

@SuppressWarnings("UnstableApiUsage")
public class EventService {
    private EventRepository eventRepository = new EventRepositoryImpl();

    public void setEnter(long userId) {
        eventRepository.setEvent(userId, "ENTER");
    }

    public void setExit(long userId) {
        eventRepository.setEvent(userId, "LOGOUT");
    }

}
