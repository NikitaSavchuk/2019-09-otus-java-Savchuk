package ru.otus;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.dao.UserDao;
import ru.otus.api.model.Address;
import ru.otus.api.model.Phone;
import ru.otus.api.model.User;
import ru.otus.api.service.DBServiceCachedUser;
import ru.otus.api.service.UserAuthService;
import ru.otus.cache.MyCache;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.UserDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.TemplateProcessorImpl;
import ru.otus.servlet.AdminServlet;
import ru.otus.servlet.AuthorizationFilter;
import ru.otus.servlet.LoginServlet;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
    Полезные для демо ссылки
    // Стартовая страница
    http://localhost:8080
    // Страница пользователей
    http://localhost:8080/users
    // REST сервис
    http://localhost:8080/api/user/3
*/
public class WebServerDemo {
    private static Logger logger = LoggerFactory.getLogger(WebServerDemo.class);
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    private static final UserAuthService USER_AUTH_SERVICE = new UserAuthService();
    private DBServiceCachedUser dbServiceCachedUser;

    public static void main(String[] args) throws Exception {
        WebServerDemo main = new WebServerDemo();

        main.startHibernate();
        main.startWebServer();
    }

    private void startWebServer() throws Exception {
        ResourceHandler resourceHandler = new ResourceHandler();
        Resource resource = Resource.newClassPathResource("/static");
        resourceHandler.setBaseResource(resource);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        context.addServlet(new ServletHolder(new LoginServlet(USER_AUTH_SERVICE, templateProcessor)), "/login");
        context.addServlet(new ServletHolder(new AdminServlet(dbServiceCachedUser, templateProcessor)), "/admin");

        context.addFilter(new FilterHolder(new AuthorizationFilter()), "/admin", null);

        Server server = new Server(WEB_SERVER_PORT);
        server.setHandler(new HandlerList(resourceHandler, context));

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
