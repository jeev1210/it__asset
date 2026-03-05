package com.example.itassetmanagement.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String root() {
        return "redirect:/home";
    }

    @GetMapping({"/home", "/index"})
    public String home(HttpServletRequest request) {
        // KILL ANY OLD SESSION FIRST
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        // ALSO KILL SPRING SECURITY CONTEXT
        SecurityContextHolder.clearContext();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // ONLY redirect if REALLY logged in with proper role
        if (auth != null
                && auth.isAuthenticated()
                && auth.getPrincipal() != null
                && !"anonymousUser".equals(auth.getPrincipal().toString())) {

            boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));

            if (isAdmin) {
                return "redirect:/admin/dashboard";
            } else {
                return "redirect:/employee/dashboard";
            }
        }

        // FORCE SHOW LANDING PAGE NO MATTER WHAT
        return "index";
    }
}