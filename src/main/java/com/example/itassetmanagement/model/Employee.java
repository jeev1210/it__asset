package com.example.itassetmanagement.model;

import com.example.itassetmanagement.model.enums.EmployeeStatus;
import com.example.itassetmanagement.model.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    private String department;
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeeStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    // ==============================
    // AUTOMATIC DEFAULTS
    // ==============================
    @PrePersist
    public void prePersist() {
        if (this.status == null) {
            this.status = EmployeeStatus.PENDING;
        }
        if (this.role == null) {
            this.role = Role.EMPLOYEE;
        }
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}
