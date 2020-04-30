package ru.otus.controllers;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.otus.domain.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class UserPackage implements Serializable {

    private List<User> users;
    private String status;

    public UserPackage() {
        this.status = "";
        this.users = new ArrayList<>();
    }
}
