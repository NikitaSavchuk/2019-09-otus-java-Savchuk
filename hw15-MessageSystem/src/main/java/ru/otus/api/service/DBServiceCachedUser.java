package ru.otus.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.otus.api.dao.UserDao;
import ru.otus.cache.MyCache;
import ru.otus.domain.User;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Service
public class DBServiceCachedUser extends DbServiceUserImpl {

    private final MyCache<String, User> entityCache;

    @Autowired
    public DBServiceCachedUser(UserDao userDao, MyCache<String, User> entityCache) {
        super(userDao);
        this.entityCache = entityCache;
    }

    public DBServiceCachedUser(UserDao userDao) {
        super(userDao);
        this.entityCache = new MyCache<>();
    }

    @Override
    public long saveUser(User user) {
        long id = super.saveUser(user);
        entityCache.put(String.valueOf(user.getId()), user);
        return id;
    }

    @Override
    public Optional<User> getUser(long id) {
        return entityCache.get(String.valueOf(id)).or(() -> super.getUser(id).map(user -> {
            entityCache.put(String.valueOf(id), user);
            return user;
        }));
    }
}
