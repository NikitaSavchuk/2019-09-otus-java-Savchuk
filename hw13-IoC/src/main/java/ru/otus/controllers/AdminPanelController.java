package ru.otus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.otus.api.service.DBServiceCachedUser;
import ru.otus.domain.User;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/users")
public class AdminPanelController {

    private DBServiceCachedUser serviceUser;

    public AdminPanelController(DBServiceCachedUser serviceUser) {
        this.serviceUser = serviceUser;
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

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user, HttpServletRequest request) {
        serviceUser.saveUser(user);
        return "redirect:list";
    }
}
