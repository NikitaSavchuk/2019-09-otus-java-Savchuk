package ru.otus.controllers;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.otus.domain.User;
import ru.otus.frontend.FrontendService;

@Controller
@RequiredArgsConstructor
public class UserDataMessageController {

    private static final Logger logger = LoggerFactory.getLogger(UserDataMessageController.class);

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final FrontendService frontendService;

    @MessageMapping("/authUserRequest")
    public void handleAuthUserRequest(User user){
        logger.info("user data message : {}", user);
        frontendService.authUser(user, this::handleAuthUserResponse);
    }

    @MessageMapping("/addUserRequest")
    public void handleAddUserRequest(User user){
        logger.info("handleAddUserMessage, user data : {}", user);
        frontendService.addUser(user, this::handleAddUserResponse);
    }

    @MessageMapping("/delUserRequest")
    public void handleDelUserRequest(User user){
        logger.info("handleDelUserMessage, user data : {}", user);
        frontendService.delUser(user, this::handleDelUserResponse);
    }

    private void handleAuthUserResponse(UserPackage data){
        simpMessagingTemplate.convertAndSend(
                "/topic/authResponse",
                data
        );
    }

    private void handleAddUserResponse(UserPackage data){
        simpMessagingTemplate.convertAndSend(
                "/topic/addUserResponse",
                data
        );
    }

    private void handleDelUserResponse(UserPackage data){
        simpMessagingTemplate.convertAndSend(
                "/topic/delUserResponse",
                data
        );
    }
}
