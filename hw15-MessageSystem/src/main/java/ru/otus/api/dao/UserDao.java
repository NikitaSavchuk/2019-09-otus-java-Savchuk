package ru.otus.api.dao;

import ru.otus.api.sessionManager.SessionManager;
import ru.otus.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    Optional<User> loadRecord(long id);

    List<User> loadRecord(String login);

    List<User> loadAll();

    boolean createRecord(User user);

    boolean updateRecord(User user);

    boolean deleteRecord(String login);

    SessionManager getSessionManager();
}
