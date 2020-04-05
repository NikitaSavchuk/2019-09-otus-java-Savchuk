package ru.otus.frontend;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import ru.otus.domain.User;
import ru.otus.messagesystem.FrontendClient;

@Controller
@RequiredArgsConstructor
public class ApiController {
    private final FrontendClient frontendClient;

    @MessageMapping({"/create"})
    public void createUser(User user) {
        frontendClient.createUser(user);
    }

    @MessageMapping({"/list"})
    public void returnUserList() {
        frontendClient.showAllUserList();
    }
}
