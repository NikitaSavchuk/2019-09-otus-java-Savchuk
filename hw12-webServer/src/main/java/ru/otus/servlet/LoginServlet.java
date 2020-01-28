package ru.otus.servlet;

import ru.otus.api.service.UserAuthService;
import ru.otus.services.TemplateProcessor;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static javax.servlet.http.HttpServletResponse.*;

public class LoginServlet extends HttpServlet {

    private static final String PARAM_LOGIN = "username";
    private static final String PARAM_PASSWORD = "password";
    private static final String LOGIN_PAGE_TEMPLATE = "login.html";
    private static final String AUTH_RESULT_VARIABLE_NAME = "resultMessage";

    private final TemplateProcessor templateProcessor;
    private final UserAuthService userAuthService;

    public LoginServlet(UserAuthService userAuthService, TemplateProcessor templateProcessor) {
        this.userAuthService = userAuthService;
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> page = new HashMap<>();
        page.put(AUTH_RESULT_VARIABLE_NAME, "");

        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(templateProcessor.getPage(LOGIN_PAGE_TEMPLATE, page));
        response.setStatus(SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String name = request.getParameter(PARAM_LOGIN);
        String password = request.getParameter(PARAM_PASSWORD);

        //если корректный логин и пароль, тогда открываем сессию и переходим на страницу админа
        if (userAuthService.isLoginAndPasswordCorrect(name, password)) {
            request.getSession();
            response.sendRedirect("/admin");
        } else {
            Map<String, Object> page = new HashMap<>();
            page.put(AUTH_RESULT_VARIABLE_NAME, "Authorization failed!");
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().println(templateProcessor.getPage(LOGIN_PAGE_TEMPLATE, page));
            response.setStatus(SC_UNAUTHORIZED);
        }
    }
}
