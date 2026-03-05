package com.example.itassetmanagement.service;

import com.example.itassetmanagement.model.*;
import com.example.itassetmanagement.model.enums.MaintenanceStatus;
import com.example.itassetmanagement.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final AssetRepository assetRepository;

    public MaintenanceService(MaintenanceRepository maintenanceRepository, AssetRepository assetRepository) {
        this.maintenanceRepository = maintenanceRepository;
        this.assetRepository = assetRepository;
    }

    public List<Maintenance> getAllOrderedByDate() {
        return maintenanceRepository.findAllByOrderByCreatedAtDesc();
    }

    public Maintenance findById(Long id) {
        return maintenanceRepository.findById(id).orElseThrow();
    }

    @Transactional
    public void saveMaintenance(Maintenance maintenance, Long assetId, Model model) {
        if (assetId == null || assetId <= 0) {
            model.addAttribute("error", "Please select a valid asset.");
            throw new IllegalArgumentException("Invalid asset");
        }
        Asset asset = assetRepository.findById(assetId).orElseThrow();
        maintenance.setAsset(asset);
        if (maintenance.getStatus() == null) {
            maintenance.setStatus(MaintenanceStatus.PENDING);
        }
        maintenanceRepository.save(maintenance);
    }

    @Transactional
    public void deleteById(Long id) {
        maintenanceRepository.deleteById(id);
    }
}