package com.example.final_project.service;


import com.example.final_project.model.Asset;
import com.example.final_project.model.dto.request.AssetRequest;
import com.example.final_project.model.dto.request.AssetRequestUpdate;

import java.util.List;

public interface AssetService {
    List<Asset> findALlAsset(Integer offset, Integer limit);

    List<Asset> getAllAssetsByName(String assetName, Integer offset, Integer limit);

    Asset findAssetById(Integer id);

    Asset updateAsset(Integer id, AssetRequestUpdate assetRequestUpdate);

    Asset insertAsset(AssetRequest assetRequest);

    void deleteAssetById(Integer assetId);

    Integer getTotalAssetRecords();

    Integer getTotalAssetRecordsFromSearch(String assetName);
}

