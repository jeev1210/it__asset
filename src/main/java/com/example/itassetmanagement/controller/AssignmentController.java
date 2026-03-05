package com.example.itassetmanagement.controller;

import com.example.itassetmanagement.model.Assignment;
import com.example.itassetmanagement.repository.AssignmentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/assignments") // employee view
public class AssignmentController {

    private final AssignmentRepository assignmentRepository;

    public AssignmentController(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    // Employee view: list assignments
    @GetMapping
    public String listAssignments(Model model) {
        List<Assignment> assignments = assignmentRepository.findAll();
        model.addAttribute("assignments", assignments);
        return "assignments/list"; // Employee assignment template
    }
}
