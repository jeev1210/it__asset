package com.example.itassetmanagement.repository;

import com.example.itassetmanagement.model.Employee;
import com.example.itassetmanagement.model.enums.EmployeeStatus;
import com.example.itassetmanagement.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // 🔹 Login lookup by email (returns Optional to avoid nulls)
    Optional<Employee> findByEmail(String email);

    // 🔹 Check duplicate email
    boolean existsByEmail(String email);

    // 🔹 Active employees with specific role
    List<Employee> findByRoleAndStatus(Role role, EmployeeStatus status);

    // 🔹 All employees by status (ACTIVE / INACTIVE / PENDING)
    List<Employee> findByStatus(EmployeeStatus status);

    // 🔹 All employees by role (ADMIN / EMPLOYEE)
    List<Employee> findByRole(Role role);

    // 🔹 Combined filter (example: ACTIVE EMPLOYEES)
    List<Employee> findByStatusAndRole(EmployeeStatus status, Role role);
}
