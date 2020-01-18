package ru.otus;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.dao.UserDao;
import ru.otus.api.model.Address;
import ru.otus.api.model.Phone;
import ru.otus.api.model.User;
import ru.otus.api.service.DBServiceCachedUser;
import ru.otus.cache.MyCache;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.UserDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CacheDemo {
    private static Logger logger = LoggerFactory.getLogger(CacheDemo.class);

    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory("hibernate.cfg.xml",
                User.class, Address.class, Phone.class);

        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        UserDao userDao = new UserDaoHibernate(sessionManager);

        MyCache<String, User> cache = new MyCache<>();

        DBServiceCachedUser dbServiceCachedUser = new DBServiceCachedUser(userDao, cache);

        User user = new User("First", 31);
        Address address = new Address("Blagodatnaya 7");
        user.setAddress(address);

        List<Phone> phones = new ArrayList<>();
        phones.add(new Phone("+7 123 45 67", user));
        phones.add(new Phone("8 627 45 67", user));
        phones.add(new Phone("+7 812 123 45 67", user));
        user.setPhones(phones);

        long id = dbServiceCachedUser.saveUser(user);

        Optional<User> createdUserFromCache = dbServiceCachedUser.getUser(id);
        logger.info("GC");
        System.gc();

        Optional<User> createdUserAfterGc = dbServiceCachedUser.getUser(id);
        logger.info("User from Cache");

        Optional<User> firstUser = dbServiceCachedUser.getUser(1L);
    }
}
