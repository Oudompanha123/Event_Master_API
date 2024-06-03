package com.example.final_project.controller;


import com.example.final_project.model.dto.request.AssetRequest;
import com.example.final_project.model.dto.response.GetAllResponse;
import com.example.final_project.model.dto.response.GetResponse;
import com.example.final_project.model.dto.response.PostResponse;
import com.example.final_project.service.AssetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/assets")
@SecurityRequirement(name = "bearerAuth")
public class AssetController {
    private final AssetService assetService;

    @GetMapping
    @Operation(summary = "Get all assets")
    public ResponseEntity<?> getAllAsset(
            @RequestParam(value = "offset",defaultValue = "1") @Positive @NotNull Integer offset,
            @RequestParam(value = "limit",defaultValue = "8") @Positive @NotNull Integer limit
    ) {
        return GetAllResponse.getAllResponse("Get all assets successful",
                assetService.getTotalAssetRecords(), assetService.findALlAsset(offset,limit));
    }

    @GetMapping("/search/{name}")
    @Operation(summary = "Search all assets by name")
    public ResponseEntity<?> getAssetByName(
            @PathVariable String name,
            @RequestParam(value = "offset",defaultValue = "1") @Positive @NotNull Integer offset,
            @RequestParam(value = "limit",defaultValue = "8") @Positive @NotNull Integer limit
    ) {
        return GetAllResponse.getAllResponse("Find asset by name successful",
                assetService.getTotalAssetRecordsFromSearch(name), assetService.getAllAssetsByName(name, offset, limit));
    }

    @GetMapping("/{assetId}")
    @Operation(summary = "Get asset by id")
    public ResponseEntity<?> getAssetById(@PathVariable("assetId") @Positive @NotNull Integer assetId) {
        return GetResponse.getResponse("Find asset by id successfully",assetService.findAssetById(assetId));
    }

    @PutMapping("/update/{assetId}")
    @Operation(summary = "Update asset by id")
    public ResponseEntity<?> createAsset(
            @PathVariable("assetId") @Positive @NotNull Integer id,
            @RequestBody @Valid AssetRequest assetRequest
    ) {
        return  GetResponse.getResponse("Update asset by id successfully",assetService.updateAsset(id, assetRequest));
    }

    @PostMapping("/create")
    @Operation(summary = "Create asset")
    public ResponseEntity<?> createAsset(@RequestBody @Valid AssetRequest assetRequest) {
        return PostResponse.postResponse("Create asset is successfully",assetService.insertAsset(assetRequest));

    }

    @DeleteMapping("/delete/{assetId}")
    @Operation(summary = "Delete asset by id successfully")
    public ResponseEntity<?> deleteMemberById(@PathVariable @Positive @NotNull Integer assetId){
        assetService.deleteAssetById(assetId);
        return GetResponse.getResponse("Delete member successfully", null);
    }
}

