package ru.otus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.api.service.DbServiceUserImpl;

@Configuration
public class DBConfig {

    private final DbServiceUserImpl serviceUser;

    public DBConfig(DbServiceUserImpl serviceUser) {
        this.serviceUser = serviceUser;
    }

    @Bean(initMethod = "init")
    public InitDefaultUsersService initDefaultUsersServiceImpl () {
        return new InitDefaultUsersServiceImpl(serviceUser);
    }
}
