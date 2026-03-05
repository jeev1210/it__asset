package com.example.itassetmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * AuthController – Only responsible for showing login page
 * Spring Security handles the actual authentication (POST /auth/login)
 * No more manual login logic → fully database-driven, no hardcoding!
 */
@Controller
@RequestMapping("/auth")
public class AuthController {

    /**
     * Show the login page
     * GET /auth/login
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // → src/main/resources/templates/login.html
    }

    /**
     * Optional: Custom logout redirect (Spring Security already handles /auth/logout)
     * You can delete this method if you want – it works either way
     */
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/auth/login?logout=true";
    }
}