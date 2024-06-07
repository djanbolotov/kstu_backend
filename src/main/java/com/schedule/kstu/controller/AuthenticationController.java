package com.schedule.kstu.controller;

import com.schedule.kstu.mapper.AuthenticationResponse;
import com.schedule.kstu.model.User;
import com.schedule.kstu.service.auth.AuthenticationService;
import com.schedule.kstu.model.Role;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/add-user")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", Role.values());
        return "add-user";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", Role.values());
        return "register";
    }

    @GetMapping("all-users")
    public String getAllUsers(Model model) {
        model.addAttribute("users", authenticationService.getAllUsers());
        return "all-users";
    }

    @PostMapping("/add-user")
    public String addUser(@ModelAttribute User user, Model model) {
        AuthenticationResponse response = authenticationService.register(user);
        model.addAttribute("response", response);
        return "redirect:/all-users";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {
        AuthenticationResponse response = authenticationService.register(user);
        model.addAttribute("response", response);
        return "index";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, Model model) {
        AuthenticationResponse response = authenticationService.authenticate(user);
        model.addAttribute("response", response);
        return "index";
    }
}
