package ru.otus.messagesystem;

import ru.otus.domain.User;

public interface DBClient extends Addressee {
    void createUser(User user);

    void getAllUserList();
}
