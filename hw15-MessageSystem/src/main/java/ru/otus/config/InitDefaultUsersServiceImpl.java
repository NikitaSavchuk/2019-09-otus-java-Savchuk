package ru.otus.config;

import ru.otus.api.service.DBServiceUserImpl;
import ru.otus.domain.User;

public class InitDefaultUsersServiceImpl {

    private final DBServiceUserImpl serviceUser;

    InitDefaultUsersServiceImpl(DBServiceUserImpl dbServiceUser) {
        this.serviceUser = dbServiceUser;
    }

    public void init() {
        serviceUser.createRecord(new User(0, "admin", "admin", true));
    }
}
