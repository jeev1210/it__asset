package com.example.itassetmanagement.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "assignments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // RELATIONSHIPS
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;

    // DATES
    @Column(name = "assigned_at", nullable = false)
    private LocalDate assignedAt = LocalDate.now();

    @Column(name = "returned_at")
    private LocalDate returnedAt;

    // OTHER FIELDS
    private String notes;

    @Column(name = "is_returned", nullable = false)
    private boolean returned = false;
}
