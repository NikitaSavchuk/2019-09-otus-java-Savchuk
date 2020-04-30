package ru.otus.messagesystem;

public interface MessageSystem {
    void addClient(MsClient msClient);

    void removeClient(String clientId);

    boolean newMessage(Message message);

    void dispose() throws InterruptedException;
}
