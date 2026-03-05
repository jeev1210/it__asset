package com.example.itassetmanagement.controller;

import com.example.itassetmanagement.model.Employee;
import com.example.itassetmanagement.service.RegistrationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "register";
    }

    @PostMapping("/register")
    public String registerEmployee(@ModelAttribute Employee employee) {
        registrationService.registerNewEmployee(employee);
        return "redirect:/register?success";
    }

    // Admin approves pending registration
    @PostMapping("/admin/approve/{id}")
    public String approve(@PathVariable Long id) {
        registrationService.approveEmployee(id);
        return "redirect:/admin/employees";  // or your pending list page
    }

    // Admin rejects registration
    @PostMapping("/admin/reject/{id}")
    public String reject(@PathVariable Long id) {
        registrationService.rejectEmployee(id);
        return "redirect:/admin/employees";
    }
}