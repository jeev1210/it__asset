package com.example.itassetmanagement.repository;

import com.example.itassetmanagement.model.Asset;
import com.example.itassetmanagement.model.Employee;
import com.example.itassetmanagement.model.enums.AssetStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, Long> {

    List<Asset> findByStatus(AssetStatus status);

    List<Asset> findByAssetTagContainingIgnoreCase(String keyword);

    // Unassigned assets
    List<Asset> findByAssignedToIsNull();

    // Assigned to employee
    List<Asset> findByAssignedTo(Employee employee);
}
