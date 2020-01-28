package ru.otus.api.service;

public class UserAuthService {

    public boolean isLoginAndPasswordCorrect(String username, String password) {
        return (username.equals("admin") && password.equals("admin123"));
    }
}
