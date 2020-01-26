package ru.otus;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import ru.otus.api.service.DBServiceCachedUser;
import ru.otus.api.service.UserAuthService;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.TemplateProcessorImpl;
import ru.otus.servlet.AdminServlet;
import ru.otus.servlet.AuthorizationFilter;
import ru.otus.servlet.LoginServlet;

import java.io.IOException;

class ServerManager {
    private static final String TEMPLATES_DIR = "/templates/";
    private static final String COMMON_RESOURCES_DIR = "/static";
    private static final String ROLE_NAME_ADMIN = "/admin";

    private final UserAuthService USER_AUTH_SERVICE;
    private final int WEB_SERVER_PORT;

    ServerManager(int port, UserAuthService user_auth_service) {
        WEB_SERVER_PORT = port;
        USER_AUTH_SERVICE = user_auth_service;
    }

    Server createServer(DBServiceCachedUser dbServiceCachedUser) throws IOException {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        context.addServlet(new ServletHolder(new LoginServlet(USER_AUTH_SERVICE, templateProcessor)), "/login");
        context.addServlet(new ServletHolder(new AdminServlet(dbServiceCachedUser, templateProcessor)), ROLE_NAME_ADMIN);

        context.addFilter(new FilterHolder(new AuthorizationFilter()), ROLE_NAME_ADMIN, null);

        Server server = new Server(WEB_SERVER_PORT);
        server.setHandler(new HandlerList(createResourceHandler(), context));
        return server;
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        Resource resource = Resource.newClassPathResource(COMMON_RESOURCES_DIR);
        resourceHandler.setBaseResource(resource);
        return resourceHandler;
    }
}
