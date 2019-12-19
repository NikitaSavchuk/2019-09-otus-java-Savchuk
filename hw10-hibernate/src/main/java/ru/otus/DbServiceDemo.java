package ru.otus;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.dao.UserDao;
import ru.otus.api.model.Address;
import ru.otus.api.model.Phone;
import ru.otus.api.model.User;
import ru.otus.api.service.DBServiceUser;
import ru.otus.api.service.DbServiceUserImpl;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.UserDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DbServiceDemo {
    private static Logger logger = LoggerFactory.getLogger(DbServiceDemo.class);

    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory("hibernate.cfg.xml",
                User.class, Address.class, Phone.class);

        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        UserDao userDao = new UserDaoHibernate(sessionManager);
        DBServiceUser dbServiceUser = new DbServiceUserImpl(userDao);

        User user = new User("First", 31);
        Address address = new Address("Blagodatnaya 7");
        user.setAddress(address);
        List<Phone> phones = new ArrayList<>();
        phones.add(new Phone("+7 123 45 67", user));
        phones.add(new Phone("8 627 45 67", user));
        phones.add(new Phone("+7 812 123 45 67", user));
        user.setPhones(phones);

        long id = dbServiceUser.saveUser(user);

        Optional<User> createdUser = dbServiceUser.getUser(id);
        User loadedUser = createdUser.get();
        System.out.println(loadedUser.getName());
        System.out.println(loadedUser.getAge());
        System.out.println(loadedUser.getPhones().get(0));
        System.out.println(loadedUser.getPhones().get(1));
        System.out.println(loadedUser.getPhones().get(2));
        System.out.println(loadedUser.getAddress());
        System.out.println();
        System.out.println(loadedUser);
    }
}
