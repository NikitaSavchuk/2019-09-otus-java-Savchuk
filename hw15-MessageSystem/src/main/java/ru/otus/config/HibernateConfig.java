package ru.otus.config;

import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.domain.User;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.sessionManager.SessionManagerHibernate;

@Configuration
@ComponentScan
@RequiredArgsConstructor
public class HibernateConfig {
    private final ApplicationContext applicationContext;

    @Bean
    public SessionManagerHibernate sessionManagerHibernate(){
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory("hibernate.cfg.xml",
                User.class);
        return new SessionManagerHibernate(sessionFactory);
    }
}
