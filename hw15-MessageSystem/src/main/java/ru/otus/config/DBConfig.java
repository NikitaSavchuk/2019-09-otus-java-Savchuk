package ru.otus.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.api.service.DBServiceUserImpl;

@Configuration
@RequiredArgsConstructor
public class DBConfig {

    private final DBServiceUserImpl serviceUser;

    @Bean(initMethod = "init")
    public InitDefaultUsersServiceImpl initDefaultUsersServiceImpl () {
        return new InitDefaultUsersServiceImpl(serviceUser);
    }
}
