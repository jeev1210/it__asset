// src/main/java/com/example/itassetmanagement/config/WebSecurityConfig.java

package com.example.itassetmanagement.config;

import com.example.itassetmanagement.config.CustomAuthenticationFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    // Inject the custom failure handler we created
    @Autowired
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF only for H2 console (safe in dev)
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                )

                // PERMIT THESE URLs WITHOUT LOGIN
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",                     // landing page
                                "/home",
                                "/index",
                                "/auth/**",              // login, logout
                                "/register",             // registration page
                                "/css/**",
                                "/js/**",
                                "/images/**"
                        ).permitAll()
                        .anyRequest().authenticated()  // everything else needs login
                )

                // FORM LOGIN CONFIG
                .formLogin(form -> form
                        .loginPage("/auth/login")                    // your custom login page
                        .loginProcessingUrl("/auth/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .successHandler((request, response, authentication) -> {
                            // Save username in session for dashboards
                            request.getSession().setAttribute("username", authentication.getName());

                            String role = authentication.getAuthorities().iterator().next().getAuthority();

                            if ("ROLE_ADMIN".equals(role)) {
                                response.sendRedirect("/admin/dashboard");
                            } else {
                                response.sendRedirect("/employee/dashboard");
                            }
                        })
                        .failureHandler(customAuthenticationFailureHandler)  // ← CUSTOM MESSAGE FOR PENDING USERS
                        .permitAll()
                )

                // LOGOUT → GO BACK TO LANDING PAGE
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )

                // For H2 console in dev (allows frames)
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }
}