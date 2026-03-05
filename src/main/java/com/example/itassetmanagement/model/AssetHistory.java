package com.example.itassetmanagement.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "asset_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;

    private String event; // e.g., "ASSIGNED", "RETURNED", "MAINTENANCE_STARTED"
    private String description;

    @ManyToOne
    private Employee performedBy;

    private LocalDateTime timestamp = LocalDateTime.now();
}