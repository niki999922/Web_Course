package ru.itmo.webmail.model.repository.impl;

import ru.itmo.webmail.model.database.DatabaseUtils;
import ru.itmo.webmail.model.repository.EventRepository;


public class EventRepositoryImpl implements EventRepository {
    @Override
    public void setEvent(long userId, String activ) {
        DatabaseUtils.process("Can't save Event.", "INSERT INTO Event (userId, type, creationTime) VALUES (?, ?, NOW());", DatabaseUtils.QueryType.INSERT, String.valueOf(userId), activ);
    }
}
