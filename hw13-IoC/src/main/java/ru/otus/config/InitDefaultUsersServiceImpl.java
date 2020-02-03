package ru.otus.config;

import org.springframework.stereotype.Service;
import ru.otus.api.service.DbServiceUserImpl;
import ru.otus.domain.User;

@Service
public class InitDefaultUsersServiceImpl implements InitDefaultUsersService {

    private final DbServiceUserImpl serviceUser;

    InitDefaultUsersServiceImpl(DbServiceUserImpl dbServiceUser) {
        this.serviceUser = dbServiceUser;
    }

    public void init() {
        User initialUser = new User("First", "First", 15);
        serviceUser.saveUser(initialUser);

        initialUser = new User("Second", "Second", 51);
        serviceUser.saveUser(initialUser);
    }
}
