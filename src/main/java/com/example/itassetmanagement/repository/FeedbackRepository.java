package com.example.itassetmanagement.repository;

import com.example.itassetmanagement.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    // Fetch all feedback for a specific employee
    List<Feedback> findAllByEmployeeId(Long employeeId);
    List<Feedback> findByEmployeeId(Long employeeId);


    // Optional: fetch all feedback ordered by submission date descending
    List<Feedback> findAllByOrderBySubmittedAtDesc();
}
