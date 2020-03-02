package ru.otus.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.otus.api.service.DBServiceCachedUser;
import ru.otus.frontend.FrontendService;
import ru.otus.domain.User;

@Slf4j
@Controller
@RequestMapping("/users")
public class AdminPanelController {

    private DBServiceCachedUser serviceUser;
    private FrontendService frontendService;
    private SimpMessagingTemplate messageSender;

    public AdminPanelController(DBServiceCachedUser serviceUser, FrontendService frontendService, SimpMessagingTemplate messageSender) {
        this.serviceUser = serviceUser;
        this.frontendService = frontendService;
        this.messageSender = messageSender;
    }

    @GetMapping("/list")
    public String listUsers(Model model) {
        model.addAttribute("users", serviceUser.getUsersList());
        return "users-list";
    }

    @GetMapping("/formForAdd")
    public String showForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "user-form";
    }

    @MessageMapping("/createUserMessage")
    public void saveUser(String frontMessage) {
        log.info("Получено сообщение от фронта: {}", frontMessage);

        frontendService.saveUser(frontMessage, userData -> {
            log.info("DBService ответил сообщением: {}", userData);
            sendFrontMessage(userData);
        });
    }

    //сообщения в WebSocket из DBService
    private void sendFrontMessage(String frontMessage) {
        messageSender.convertAndSend("/topic/DBServiceResponse", frontMessage);
    }
}
