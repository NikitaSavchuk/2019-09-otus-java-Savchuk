package ru.otus.messagesystem;

import java.util.Optional;

public interface ReqRespHandler {
    Optional<Message> handle(Message message);
}
