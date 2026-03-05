package com.example.itassetmanagement.service;

import com.example.itassetmanagement.model.*;
import com.example.itassetmanagement.model.enums.AssetStatus;
import com.example.itassetmanagement.repository.AssetHistoryRepository;
import com.example.itassetmanagement.repository.AssetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AssetService {

    private final AssetRepository assetRepository;
    private final AssetHistoryRepository assetHistoryRepository;

    public AssetService(AssetRepository assetRepository,
                        AssetHistoryRepository assetHistoryRepository) {
        this.assetRepository = assetRepository;
        this.assetHistoryRepository = assetHistoryRepository;
    }

    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    public List<Asset> getAssetsByStatus(AssetStatus status) {
        return assetRepository.findByStatus(status);
    }

    public List<Asset> searchByAssetTag(String keyword) {
        return assetRepository.findByAssetTagContainingIgnoreCase(keyword);
    }

    public List<Asset> getUnassignedAssets() {
        return assetRepository.findByAssignedToIsNull();
    }

    public List<Asset> getAssetsByAssignedEmployee(Employee employee) {
        return assetRepository.findByAssignedTo(employee);
    }

    public List<AssetHistory> getHistoryByAssetId(Long assetId) {
        return assetHistoryRepository.findByAssetIdOrderByTimestampDesc(assetId);
    }

    @Transactional
    public Asset saveAsset(Asset asset) {
        return assetRepository.save(asset);
    }

    @Transactional
    public void deleteAsset(Long id) {
        assetRepository.deleteById(id);
    }

    public Asset findById(Long id) {
        return assetRepository.findById(id).orElse(null);
    }
    // Inside your existing AssetService.java

    public long countAll() {
        return assetRepository.count();
    }

    @Transactional
    public Asset createAsset(Asset asset) {
        if (asset.getStatus() == null) {
            asset.setStatus(AssetStatus.AVAILABLE);
        }
        return assetRepository.save(asset);
    }

    @Transactional
    public Asset updateAsset(Asset asset) {
        return assetRepository.save(asset);
    }

    public List<Asset> getAvailableAssets() {
        return assetRepository.findAll()
                .stream()
                .filter(a -> a.getStatus() == null || a.getStatus() == AssetStatus.AVAILABLE)
                .toList();
    }
}