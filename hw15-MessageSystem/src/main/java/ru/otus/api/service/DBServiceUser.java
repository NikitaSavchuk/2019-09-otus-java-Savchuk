package ru.otus.api.service;

import ru.otus.domain.User;

import java.util.List;
import java.util.Optional;

public interface DBServiceUser {

    long saveUser(User user);

    Optional<User> getUser(long id);

    List<User> getUsersList();
}
