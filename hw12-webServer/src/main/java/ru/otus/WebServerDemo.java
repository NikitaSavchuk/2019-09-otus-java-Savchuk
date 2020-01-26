package ru.otus;

import org.eclipse.jetty.server.Server;
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

/*
    Полезные для демо ссылки
    // Стартовая страница
    http://localhost:8080
*/
public class WebServerDemo {
    private static Logger logger = LoggerFactory.getLogger(WebServerDemo.class);
    private DBServiceCachedUser dbServiceCachedUser;

    public static void main(String[] args) throws Exception {
        WebServerDemo main = new WebServerDemo();

        main.startHibernate();
        main.startWebServer();
    }

    private void startWebServer() throws Exception {
        ServerManager serverManager = new ServerManager();
        Server server = serverManager.createServer(dbServiceCachedUser);

        server.start();
        server.join();
    }

    private void startHibernate() {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory("hibernate.cfg.xml",
                User.class, Address.class, Phone.class);

        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        UserDao userDao = new UserDaoHibernate(sessionManager);

        MyCache<String, User> cache = new MyCache<>();

        dbServiceCachedUser = new DBServiceCachedUser(userDao, cache);

        User user = new User("First", 31);
        Address address = new Address("Blagodatnaya 7");
        user.setAddress(address);

        List<Phone> phones = new ArrayList<>();
        phones.add(new Phone("+7 123 45 67", user));
        phones.add(new Phone("8 627 45 67", user));
        phones.add(new Phone("+7 812 123 45 67", user));
        user.setPhones(phones);

        dbServiceCachedUser.saveUser(user);
    }
}
