package com.example.final_project.service.Impl;


import com.example.final_project.exception.BadRequestException;
import com.example.final_project.exception.NotFoundException;
import com.example.final_project.model.Asset;
import com.example.final_project.model.dto.request.asset.AssetRequest;
import com.example.final_project.repository.AssetRepository;
import com.example.final_project.service.AssetService;
import com.example.final_project.util.Token;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AssetServiceImpl implements AssetService {
    private final AssetRepository assetRepository;

    @Override
    public List<Asset> findALlAsset(Integer offset, Integer limit) {
        offset = (offset - 1) * limit;
        return assetRepository.findAllAssets(offset, limit, Token.getOrgIdByToken());
    }

    @Override
    public List<Asset> getAllAssetsByName(String assetName, Integer offset, Integer limit) {
        offset = (offset - 1) * limit;
        return assetRepository.getAllAssetsByName(assetName, offset, limit, Token.getOrgIdByToken());
    }

    @Override
    public Asset findAssetById(Integer id) {
        Asset asset = assetRepository.findAssetById(id, Token.getOrgIdByToken());
        if (asset == null) {
            throw new NotFoundException("Asset id : " + id + " not found");
        }
        return asset;
    }

    @Override
    public Asset insertAsset(AssetRequest assetRequest) {
        return assetRepository.insertAsset(assetRequest, Token.getOrgIdByToken());
    }

    @Override
    public void deleteAssetById(Integer assetId) {
        Asset asset = assetRepository.findAssetById(assetId, Token.getOrgIdByToken());
        if (asset == null) {
            throw new NotFoundException("Asset id : " + assetId + " not found");
        }
        assetRepository.deleteAssetById(assetId, Token.getOrgIdByToken());
    }

    @Override
    public Asset updateAsset(Integer id, AssetRequest assetRequest) {
        Asset asset = assetRepository.findAssetById(id, Token.getOrgIdByToken());
        if (asset == null) {
            throw new NotFoundException("Asset id : " + id + " not found");
        }
        return assetRepository.updateAsset(id, assetRequest, Token.getOrgIdByToken());
    }

    @Override
    public Integer getTotalAssetRecords() {
        return assetRepository.getTotalAssetRecords(Token.getOrgIdByToken());
    }

    @Override
    public Integer getTotalAssetRecordsFromSearch(String assetName) {
        return assetRepository.getTotalAssetRecordsFromSearch(assetName, Token.getOrgIdByToken());
    }

}
