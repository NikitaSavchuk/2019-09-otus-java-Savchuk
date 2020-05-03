package ru.otus.config;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.domain.User;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.sessionManager.SessionManagerHibernate;

@Configuration
@ComponentScan
public class HibernateConfig {

    @Bean
    public SessionManagerHibernate sessionManagerHibernate(){
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory("hibernate.cfg.xml",
                User.class);
        return new SessionManagerHibernate(sessionFactory);
    }
}
