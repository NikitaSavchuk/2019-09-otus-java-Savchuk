package ru.otus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.backend.executor.DbExecutorHibernate;
import ru.otus.backend.executor.DbHibernateExecutorHibernateImpl;
import ru.otus.domain.User;

@Configuration
public class BackendConfig {

    @Bean
    public DbExecutorHibernate<User> dbService() {
        return new DbHibernateExecutorHibernateImpl<>();
    }
}
