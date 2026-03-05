package com.example.itassetmanagement.repository;

import com.example.itassetmanagement.model.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Maintenance entity
 * Provides CRUD operations + custom query to get latest first
 */
@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {

    /**
     * Find all maintenance records ordered by creation date (newest first)
     */
    List<Maintenance> findAllByOrderByCreatedAtDesc();

    /**
     * Optional: Find maintenance by asset ID (useful later)
     */
    List<Maintenance> findByAssetIdOrderByCreatedAtDesc(Long assetId);
}