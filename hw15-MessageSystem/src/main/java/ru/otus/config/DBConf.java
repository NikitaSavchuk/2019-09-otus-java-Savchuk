package ru.otus.config;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.api.service.DBServiceUser;
import ru.otus.domain.User;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class DBConf {

    private final DBServiceUser serviceUser;

    @PostConstruct
    public void init(){
        serviceUser.createRecord(new User(0, "admin", "admin", true));
    }
}
