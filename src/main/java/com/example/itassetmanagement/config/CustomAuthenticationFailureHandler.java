// src/main/java/com/example/itassetmanagement/config/CustomAuthenticationFailureHandler.java

package com.example.itassetmanagement.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        String errorMessage = "Invalid email or password.";

        // When user is disabled (PENDING), Spring throws DisabledException
        if (exception.getCause() instanceof org.springframework.security.authentication.DisabledException ||
                exception instanceof org.springframework.security.authentication.DisabledException) {
            errorMessage = "Account pending admin approval.";
        }

        // Store message in session so login page can show it
        request.getSession().setAttribute("loginError", errorMessage);

        // Redirect back to login with error param
        setDefaultFailureUrl("/auth/login?error=true");
        super.onAuthenticationFailure(request, response, exception);
    }
}