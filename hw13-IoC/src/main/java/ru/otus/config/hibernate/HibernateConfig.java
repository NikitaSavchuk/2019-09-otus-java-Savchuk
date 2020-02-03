package ru.otus.config.hibernate;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.*;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.otus.domain.User;
import ru.otus.hibernate.HibernateUtils;

@Configuration
@EnableTransactionManagement
@ImportResource({"classpath:hibernate.cfg.xml"})
public class HibernateConfig {

    @Bean
    public SessionFactory sessionFactory() {
        return HibernateUtils.buildSessionFactory("hibernate.cfg.xml", User.class);
    }
}
