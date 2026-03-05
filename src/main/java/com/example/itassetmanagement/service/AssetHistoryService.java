package com.example.itassetmanagement.service;

import com.example.itassetmanagement.model.Asset;
import com.example.itassetmanagement.model.AssetHistory;
import com.example.itassetmanagement.model.Employee;
import com.example.itassetmanagement.repository.AssetHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetHistoryService {

    private final AssetHistoryRepository assetHistoryRepository;

    public AssetHistoryService(AssetHistoryRepository assetHistoryRepository) {
        this.assetHistoryRepository = assetHistoryRepository;
    }

    // THIS IS THE MISSING METHOD — ADD THIS!
    public List<AssetHistory> getByAssetId(Long assetId) {
        return assetHistoryRepository.findByAssetIdOrderByTimestampDesc(assetId);
    }

    // Optional: Add a method to log history (you probably already have this)
    public void logHistory(Asset asset, String event, String description, Employee performedBy) {
        AssetHistory history = AssetHistory.builder()
                .asset(asset)
                .event(event)
                .description(description)
                .performedBy(performedBy)
                .timestamp(java.time.LocalDateTime.now())
                .build();
        assetHistoryRepository.save(history);
    }
}