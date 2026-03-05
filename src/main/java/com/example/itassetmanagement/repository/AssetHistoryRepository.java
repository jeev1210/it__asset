package com.example.itassetmanagement.repository;

import com.example.itassetmanagement.model.AssetHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link AssetHistory} entity.
 * Provides CRUD operations and custom query methods.
 */
@Repository
public interface AssetHistoryRepository extends JpaRepository<AssetHistory, Long> {

    /**
     * Retrieves all history records for a specific asset,
     * ordered from newest to oldest (by timestamp descending).
     *
     * @param assetId the ID of the asset
     * @return List of AssetHistory entries, newest first
     */
    List<AssetHistory> findByAssetIdOrderByTimestampDesc(Long assetId);
}