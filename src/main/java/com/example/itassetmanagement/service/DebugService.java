package com.example.itassetmanagement.service;

import com.example.itassetmanagement.model.Employee;
import com.example.itassetmanagement.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DebugService {

    private final EmployeeRepository employeeRepository;

    public DebugService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllUsers() {
        return employeeRepository.findAll();
    }

    @Transactional
    public void resetAllUsers() {
        employeeRepository.deleteAll();
        System.out.println("ALL USERS DELETED - READY FOR RECREATION!");
    }
}