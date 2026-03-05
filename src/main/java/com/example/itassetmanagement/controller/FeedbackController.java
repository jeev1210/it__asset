package com.example.itassetmanagement.controller;

import com.example.itassetmanagement.model.Feedback;
import com.example.itassetmanagement.service.FeedbackService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/employee/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping("/create")
    public String createFeedbackForm(HttpSession session, Model model) {
        String email = (String) session.getAttribute("username");
        if (email == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("feedback", new Feedback());
        return "employee/feedback-create";
    }

    @PostMapping("/save")
    public String saveFeedback(@ModelAttribute Feedback feedback, HttpSession session) {
        String email = (String) session.getAttribute("username");
        if (email == null) {
            return "redirect:/auth/login";
        }
        feedbackService.saveFeedback(feedback, email);
        return "redirect:/employee/dashboard";
    }

    @GetMapping("/my-feedback")
    public String myFeedback(HttpSession session, Model model) {
        String email = (String) session.getAttribute("username");
        if (email == null) {
            return "redirect:/auth/login";
        }
        model.addAttribute("feedbacks", feedbackService.getMyFeedback(email));
        return "employee/dashboard";
    }
}