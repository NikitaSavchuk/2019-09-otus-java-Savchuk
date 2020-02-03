package ru.otus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.api.service.DbServiceUserImpl;
import ru.otus.domain.User;

@Configuration
public class InitDefaultUsersServiceImpl implements InitDefaultUsersService {

    private final DbServiceUserImpl serviceUser;

    public InitDefaultUsersServiceImpl(DbServiceUserImpl dbServiceUser) {
        this.serviceUser = dbServiceUser;
    }

    @Bean
    public void init() {
        User initialUser = new User("1", "2", 15);
        serviceUser.saveUser(initialUser);

        initialUser = new User("3", "4", 51);
        serviceUser.saveUser(initialUser);
    }
}
