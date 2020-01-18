package ru.otus.api.service;

import ru.otus.api.dao.UserDao;
import ru.otus.api.model.User;
import ru.otus.cache.MyCache;

import java.util.Optional;

public class DBServiceCachedUser extends DbServiceUserImpl {
    private final MyCache<String, User> entityCache;

    public DBServiceCachedUser(UserDao userDao) {
        super(userDao);
        this.entityCache = new MyCache<>();
    }

    public DBServiceCachedUser(UserDao userDao, MyCache<String, User> entityCache) {
        super(userDao);
        this.entityCache = entityCache;
    }

    @Override
    public long saveUser(User user) {
        long id = super.saveUser(user);
        entityCache.put(String.valueOf(user.getId()), user);
        return id;
    }

    @Override
    public Optional<User> getUser(long id) {
        //If we get user from DB, then we also save User in cache.
        return entityCache.get(String.valueOf(id)).or(() -> super.getUser(id).map(user -> {
            entityCache.put(String.valueOf(id), user);
            return user;
        }));
    }
}
