package com.example.itassetmanagement.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public void logout(HttpSession session) {
        session.invalidate();
    }
}