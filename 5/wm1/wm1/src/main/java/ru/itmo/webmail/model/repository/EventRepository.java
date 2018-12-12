package ru.itmo.webmail.model.repository;

import ru.itmo.webmail.model.domain.Message;
import ru.itmo.webmail.model.domain.User;

import java.util.List;

public interface EventRepository {
    void setEvent(long userId, String activ);
}
