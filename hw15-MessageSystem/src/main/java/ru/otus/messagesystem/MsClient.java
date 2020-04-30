package ru.otus.messagesystem;

public interface MsClient {
    void addHandler(MessageType type, ReqRespHandler reqRespHandler);

    boolean sendMessage(Message message);

    void handle(Message message);

    String getName();

    <T> Message produceMessage(String to, T data, MessageType messageType);
}
