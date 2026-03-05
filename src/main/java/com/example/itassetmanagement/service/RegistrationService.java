package com.example.itassetmanagement.service;

import com.example.itassetmanagement.model.Employee;
import com.example.itassetmanagement.model.enums.EmployeeStatus;
import com.example.itassetmanagement.model.enums.Role;
import com.example.itassetmanagement.repository.EmployeeRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RegistrationService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerNewEmployee(Employee employee) {
        // Hash password
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));

        // Set default values
        employee.setStatus(EmployeeStatus.PENDING);
        employee.setRole(Role.EMPLOYEE);

        employeeRepository.save(employee);
    }

    public void approveEmployee(Long id) {
        Employee emp = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        emp.setStatus(EmployeeStatus.APPROVED);
        emp.setApprovedAt(java.time.LocalDateTime.now());
        employeeRepository.save(emp);
    }

    public void rejectEmployee(Long id) {
        Employee emp = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        emp.setStatus(EmployeeStatus.REJECTED);
        employeeRepository.save(emp);
    }
}