package com.example.final_project.service.Impl;


import com.example.final_project.exception.BadRequestException;
import com.example.final_project.exception.NotFoundException;
import com.example.final_project.model.Asset;
import com.example.final_project.model.dto.request.AssetRequest;
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
        if(assetName == null || assetName.isBlank())
            return assetRepository.findAllAssets(offset, limit, Token.getOrgIdByToken());
        return assetRepository.getAllAssetsByName(assetName.trim(), offset, limit, Token.getOrgIdByToken());
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
        // qty must be greater than 0
        if(assetRequest.getQty() == 0)
            throw new BadRequestException("Qty must be greater than 0");

        // check duplicate asset name
        List<String> assetNames = assetRepository.getAllAssetNames(Token.getOrgIdByToken());
        for(String assetName : assetNames)
            if(assetName.equalsIgnoreCase(assetRequest.getAssetName()))
                throw new BadRequestException("Duplicate asset name");
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
        if (asset == null)
            throw new NotFoundException("Asset id : " + id + " not found");

        // check duplicate asset name
        List<String> assetNames = assetRepository.getAllAssetNames(Token.getOrgIdByToken());
        for(String assetName : assetNames){
            if(assetName.equalsIgnoreCase(assetRequest.getAssetName())){
                if(asset.getAssetName().equalsIgnoreCase(assetRequest.getAssetName()))
                    break;
                throw new BadRequestException("Duplicate asset name");
            }
        }

        return assetRepository.updateAsset(id, assetRequest, Token.getOrgIdByToken());
    }

    @Override
    public Integer getTotalAssetRecords() {
        return assetRepository.getTotalAssetRecords(Token.getOrgIdByToken());
    }

    @Override
    public Integer getTotalAssetRecordsFromSearch(String assetName) {
        if(assetName == null || assetName.isBlank())
            return getTotalAssetRecords();
        return assetRepository.getTotalAssetRecordsFromSearch(assetName.trim(), Token.getOrgIdByToken());
    }

}
