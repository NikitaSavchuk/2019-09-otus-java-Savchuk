package ru.otus.api.service;

import ru.otus.domain.User;

import java.util.List;
import java.util.Optional;

public interface DBServiceUser {
    boolean createRecord(User user);

    boolean updateRecord(User user);

    Optional<User> loadRecord(long id);

    List<User> loadRecord(String login);

    List<User> loadAll();

    boolean deleteRecord(String login);
}
