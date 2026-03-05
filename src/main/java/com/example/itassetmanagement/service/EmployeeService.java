package com.example.itassetmanagement.service;

import com.example.itassetmanagement.model.*;
import com.example.itassetmanagement.model.enums.EmployeeStatus;
import com.example.itassetmanagement.model.enums.Role;
import com.example.itassetmanagement.model.enums.TicketStatus;
import com.example.itassetmanagement.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional  // ← FIXED: Removed readOnly = true
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final FeedbackRepository feedbackRepository;
    private final TicketRepository ticketRepository;

    public EmployeeService(EmployeeRepository employeeRepository,
                           FeedbackRepository feedbackRepository,
                           TicketRepository ticketRepository) {
        this.employeeRepository = employeeRepository;
        this.feedbackRepository = feedbackRepository;
        this.ticketRepository = ticketRepository;
    }

    public Employee getByEmail(String email) {
        return employeeRepository.findByEmail(email).orElse(null);
    }

    public boolean existsByEmail(String email) {
        return employeeRepository.existsByEmail(email);
    }

    @Transactional
    public Employee updateProfile(Employee emp, String fullName, String phone, String department) {
        emp.setFullName(fullName);
        emp.setPhone(phone);
        emp.setDepartment(department);
        return employeeRepository.save(emp);
    }

    public List<Feedback> getFeedbacksByEmployeeId(Long employeeId) {
        return feedbackRepository.findByEmployeeId(employeeId);
    }

    public long countOpenTicketsByEmail(String email) {
        return ticketRepository.findByCreatedByEmail(email)
                .stream()
                .filter(t -> t.getStatus() == TicketStatus.OPEN)
                .count();
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public long countAll() {
        return employeeRepository.count();
    }

    public Employee findById(Long id) {
        return employeeRepository.findById(id).orElseThrow();
    }

    public void deleteById(Long id) {
        employeeRepository.deleteById(id);
    }

    public List<Employee> getPendingEmployees() {
        return employeeRepository.findByStatus(EmployeeStatus.PENDING);
    }

    @Transactional  // Ensures write transaction
    public void approveEmployee(Long id) {
        Employee emp = findById(id);
        emp.setStatus(EmployeeStatus.APPROVED);
        emp.setApprovedAt(LocalDateTime.now());  // Optional: record approval time
        employeeRepository.save(emp);
    }

    @Transactional
    public void saveEmployee(Employee employee, PasswordEncoder encoder) {
        boolean isNew = employee.getId() == null;

        if (isNew && employeeRepository.existsByEmail(employee.getEmail())) {
            throw new RuntimeException("Email already exists!");
        }

        if (isNew) {
            employee.setPassword(encoder.encode("12345678"));
            if (employee.getRole() == null) employee.setRole(Role.EMPLOYEE);
        } else {
            Employee existing = findById(employee.getId());
            employee.setPassword(existing.getPassword());
        }

        employeeRepository.save(employee);
    }

    @Transactional
    public void resetAllUsers(PasswordEncoder encoder) {
        employeeRepository.deleteAll();
        // Your original admin creation logic here if needed
    }
}