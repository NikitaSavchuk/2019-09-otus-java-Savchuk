package ru.otus.servlet;

import ru.otus.api.model.User;
import ru.otus.api.service.DBServiceCachedUser;
import ru.otus.services.TemplateProcessor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static javax.servlet.http.HttpServletResponse.*;

public class AdminServlet extends HttpServlet {
    private static final String ADMIN_PAGE_TEMPLATE = "admin.html";
    private static final String CREATED_USER_VAR_NAME = "createdUser";
    private static final String USERS_LIST_VAR_NAME = "usersList";
    private static final String TEXT_HTML_CHARSET_UTF_8 = "text/html;charset=utf-8";

    private final TemplateProcessor templateProcessor;
    private DBServiceCachedUser cachedUser;

    public AdminServlet(DBServiceCachedUser cachedUserService, TemplateProcessor templateProcessor) {
        this.cachedUser = cachedUserService;
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put(USERS_LIST_VAR_NAME, Collections.emptyList());
        pageVariables.put(CREATED_USER_VAR_NAME, Collections.emptyList());

        response.setContentType(TEXT_HTML_CHARSET_UTF_8);
        response.getWriter().println(templateProcessor.getPage(ADMIN_PAGE_TEMPLATE, pageVariables));
        response.setStatus(SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //Если в запросе была нажата кнопка "Создать нового пользователя"
        if (request.getParameter("createUser") != null) {
            doPostForCreateUser(request, response);
        } else if (request.getParameter("getUsersList") != null) {
            doPostForGetUsersList(response);
        } else if (request.getParameter("exitAdminPanel") != null) {
            exitAdminPanel(request, response);
        }
    }

    private void exitAdminPanel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().invalidate();
        response.sendRedirect("/login");
    }

    private void doPostForGetUsersList(HttpServletResponse response) throws IOException {
        Map<String, Object> page = new HashMap<>();
        page.put(CREATED_USER_VAR_NAME, Collections.emptyList());
        page.put(USERS_LIST_VAR_NAME, cachedUser.getUsersList());

        response.setContentType(TEXT_HTML_CHARSET_UTF_8);
        response.getWriter().println(templateProcessor.getPage(ADMIN_PAGE_TEMPLATE, page));
        response.setStatus(SC_OK);
    }

    private void doPostForCreateUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userName = request.getParameter("userName");
        int userAge = Integer.parseInt(request.getParameter("userAge"));

        User savedUser = new User(userName, userAge);
        cachedUser.saveUser(savedUser);

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put(CREATED_USER_VAR_NAME, Collections.singletonList(savedUser));
        pageVariables.put(USERS_LIST_VAR_NAME, Collections.emptyList());

        response.setContentType(TEXT_HTML_CHARSET_UTF_8);
        response.getWriter().println(templateProcessor.getPage(ADMIN_PAGE_TEMPLATE, pageVariables));
        response.setStatus(SC_OK);
    }
}
