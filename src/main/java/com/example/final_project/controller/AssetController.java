package com.example.final_project.controller;


import com.example.final_project.model.dto.request.asset.AssetRequest;
import com.example.final_project.model.dto.response.GetAllResponse;
import com.example.final_project.model.dto.response.GetResponse;
import com.example.final_project.model.dto.response.PostResponse;
import com.example.final_project.service.AssetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/asset")
@SecurityRequirement(name = "bearerAuth")
public class AssetController {
    private final AssetService assetService;

    @GetMapping
    @Operation(summary = "Get all assets")
    public ResponseEntity<?> getAllAsset(
            @RequestParam(value = "offset",defaultValue = "1") @Positive Integer offset,
            @RequestParam(value = "limit",defaultValue = "8") @Positive Integer limit
    ) {
        return GetAllResponse.getAllResponse("Get all assets successful", assetService.getTotalAssetRecords(), assetService.findALlAsset(offset,limit));
    }

    @GetMapping("/search/{name}")
    @Operation(summary = "Search all assets by name")
    public ResponseEntity<?> getAssetByName(
            @PathVariable String name,
            @RequestParam(value = "offset",defaultValue = "1") @Positive Integer offset,
            @RequestParam(value = "limit",defaultValue = "8") @Positive Integer limit
    ) {
        return GetAllResponse.getAllResponse("Find asset by name successful", assetService.getTotalAssetRecordsFromSearch(name), assetService.getAllAssetsByName(name, offset, limit));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get asset by id")
    public ResponseEntity<?> getAssetById(@PathVariable("id") @Positive Integer id) {
        return GetResponse.getResponse("Find asset by id successful",assetService.findAssetById(id));
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update asset by id")
    public ResponseEntity<?> createAsset(
            @PathVariable("id") Integer id,
            @RequestBody @Valid AssetRequest assetRequest
    ) {
        return  GetResponse.getResponse("Update asset by id successful",assetService.updateAsset(id, assetRequest));
    }

    @PostMapping("/create")
    @Operation(summary = "Create asset")
    public ResponseEntity<?> createAsset(@RequestBody @Valid AssetRequest assetRequest) {
        return PostResponse.postResponse("Create asset is successful",assetService.insertAsset(assetRequest));

    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete asset by id")
    public ResponseEntity<?> deleteMemberById(@PathVariable(name = "id") Integer assetId){
        assetService.deleteAssetById(assetId);
        return GetResponse.getResponse("delete member successfully", null);
    }
}

