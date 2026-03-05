package com.example.itassetmanagement.controller;

import com.example.itassetmanagement.service.DebugService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/debug")
public class DebugController {

    private final DebugService debugService;

    public DebugController(DebugService debugService) {
        this.debugService = debugService;
    }

    @GetMapping("/users")
    public String showAllUsers(Model model) {
        model.addAttribute("users", debugService.getAllUsers());
        return "debug/users";
    }

    @PostMapping("/reset-users")
    public String resetUsers() {
        debugService.resetAllUsers();
        return "redirect:/debug/users";
    }
}