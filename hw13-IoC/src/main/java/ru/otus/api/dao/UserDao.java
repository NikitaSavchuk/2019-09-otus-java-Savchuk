package ru.otus.api.dao;

import ru.otus.domain.User;
import ru.otus.api.sessionmanager.SessionManager;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    Optional<User> findById(long id);

    long saveUser(User user);

    Optional<User> loadUser(long id);

    SessionManager getSessionManager();

    List<User> getAllUsers();
}
