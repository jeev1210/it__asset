package com.example.itassetmanagement.service;

import com.example.itassetmanagement.model.Employee;
import com.example.itassetmanagement.model.Feedback;
import com.example.itassetmanagement.repository.EmployeeRepository;
import com.example.itassetmanagement.repository.FeedbackRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final EmployeeRepository employeeRepository;

    public FeedbackService(FeedbackRepository feedbackRepository,
                           EmployeeRepository employeeRepository) {
        this.feedbackRepository = feedbackRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public void saveFeedback(Feedback feedback, String email) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        feedback.setEmployee(employee);
        feedback.setSubmittedAt(LocalDateTime.now());
        feedbackRepository.save(feedback);
    }

    public List<Feedback> getMyFeedback(String email) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return feedbackRepository.findByEmployeeId(employee.getId());
    }

    // For Admin panel (already used in AdminController)
    public List<Feedback> getAllFeedbacksOrdered() {
        return feedbackRepository.findAllByOrderBySubmittedAtDesc();
    }

    @Transactional
    public void deleteById(Long id) {
        feedbackRepository.deleteById(id);
    }
}