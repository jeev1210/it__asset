package com.example.itassetmanagement.service;

import com.example.itassetmanagement.model.*;
import com.example.itassetmanagement.model.enums.AssetStatus;
import com.example.itassetmanagement.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final AssetRepository assetRepository;
    private final AssetHistoryRepository assetHistoryRepository;

    public AssignmentService(AssignmentRepository assignmentRepository,
                             AssetRepository assetRepository,
                             AssetHistoryRepository assetHistoryRepository) {
        this.assignmentRepository = assignmentRepository;
        this.assetRepository = assetRepository;
        this.assetHistoryRepository = assetHistoryRepository;
    }

    public List<Assignment> getPendingByEmployeeId(Long employeeId) {
        return assignmentRepository.findByEmployeeIdAndReturnedFalse(employeeId);
    }

    public Assignment findById(Long id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
    }

    @Transactional
    public void returnAsset(Assignment assignment, Employee returnedBy) {
        assignment.setReturned(true);
        assignment.setReturnedAt(LocalDate.now());
        assignmentRepository.save(assignment);

        Asset asset = assignment.getAsset();
        if (asset != null) {
            asset.setAssignedTo(null);
            asset.setAssignedDate(null);
            asset.setStatus(AssetStatus.AVAILABLE);
            assetRepository.save(asset);

            AssetHistory history = AssetHistory.builder()
                    .asset(asset)
                    .event("RETURNED")
                    .description("Returned by " + returnedBy.getFullName())
                    .performedBy(returnedBy)
                    .timestamp(LocalDateTime.now())
                    .build();
            assetHistoryRepository.save(history);
        }
    }
    // Inside your existing AssignmentService.java

    public long countAll() {
        return assignmentRepository.count();
    }

    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    @Transactional
    public void createAssignment(Assignment assignment) {
        assignment.setAssignedAt(LocalDate.now());
        assignmentRepository.save(assignment);

        Asset asset = assetRepository.findById(assignment.getAsset().getId())
                .orElseThrow(() -> new IllegalArgumentException("Asset not found"));

        asset.setStatus(AssetStatus.ASSIGNED);
        asset.setAssignedTo(assignment.getEmployee());
        asset.setAssignedDate(assignment.getAssignedAt());
        assetRepository.save(asset);

        AssetHistory history = AssetHistory.builder()
                .asset(asset)
                .event("ASSIGNED")
                .description("Assigned to " + assignment.getEmployee().getFullName())
                .performedBy(null)
                .timestamp(LocalDateTime.now())
                .build();
        assetHistoryRepository.save(history);
    }

    @Transactional
    public void deleteAssignment(Long id) {
        Assignment assignment = assignmentRepository.findById(id).orElseThrow();
        Asset asset = assignment.getAsset();

        asset.setStatus(AssetStatus.AVAILABLE);
        asset.setAssignedTo(null);
        asset.setAssignedDate(null);
        assetRepository.save(asset);

        assignmentRepository.deleteById(id);

        AssetHistory history = AssetHistory.builder()
                .asset(asset)
                .event("DELETED_ASSIGNMENT")
                .description("Assignment deleted by admin")
                .performedBy(null)
                .timestamp(LocalDateTime.now())
                .build();
        assetHistoryRepository.save(history);
    }

    @Transactional
    public void returnAssetFromAdmin(Long id) {
        Assignment assignment = assignmentRepository.findById(id).orElseThrow();
        assignment.setReturned(true);
        assignment.setReturnedAt(LocalDate.now());
        assignmentRepository.save(assignment);

        Asset asset = assignment.getAsset();
        asset.setStatus(AssetStatus.AVAILABLE);
        asset.setAssignedTo(null);
        asset.setAssignedDate(null);
        assetRepository.save(asset);

        AssetHistory history = AssetHistory.builder()
                .asset(asset)
                .event("RETURNED")
                .description("Returned via admin panel")
                .performedBy(assignment.getEmployee())
                .timestamp(LocalDateTime.now())
                .build();
        assetHistoryRepository.save(history);
    }
}