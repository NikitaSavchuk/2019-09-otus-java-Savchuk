package ru.otus.api.service;

public class UserAuthService {

    public static final String ADMIN = "admin";
    public static final String ADMIN_PASSWORD = "admin123";

    public boolean isLoginAndPasswordCorrect(String username, String password) {
        return (username.equals(ADMIN) && password.equals(ADMIN_PASSWORD));
    }
}
