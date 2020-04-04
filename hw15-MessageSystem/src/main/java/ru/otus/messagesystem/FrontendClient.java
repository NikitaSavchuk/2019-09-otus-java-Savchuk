package ru.otus.messagesystem;

import ru.otus.domain.User;

import java.util.List;

public interface FrontendClient extends Addressee {
    void returnUserList(List<User> userList);

    void createUser(User user);

    void showAllUserList();
}
