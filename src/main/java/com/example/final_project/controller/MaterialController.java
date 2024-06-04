package com.example.final_project.controller;

import com.example.final_project.model.Supporter;
import com.example.final_project.model.constant.Status;
import com.example.final_project.model.dto.request.material.MultipleDelete;
import com.example.final_project.model.dto.response.GetResponse;
import com.example.final_project.model.dto.response.PostResponse;
import com.example.final_project.model.dto.response.UpdateResponse;
import com.example.final_project.service.MaterialService;
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
@RequestMapping("/api/materials")
@SecurityRequirement(name = "bearerAuth")
public class MaterialController {
    private final MaterialService materialService;

    @GetMapping("/getAll/{eventId}")
    @Operation(summary = "Get all materials by event id")
    public ResponseEntity<?> getAllMaterials(@PathVariable @Positive @NotNull Integer eventId) {
        return GetResponse.getResponse("Get all materials successfully", materialService.getAllMaterials(eventId));
    }

    @GetMapping("/{materialId}")
    @Operation(summary = "Get material by id")
    public ResponseEntity<?> getMaterialById(@PathVariable @Positive @NotNull Integer materialId ) {
        return GetResponse.getResponse("Get material by id successfully",
                materialService.getMaterialById(materialId));
    }

    @GetMapping("/count-status/{eventId}")
    @Operation(summary = "Get count status")
    public ResponseEntity<?> getAllMaterialsCount(@PathVariable @Positive @NotNull Integer eventId ) {
        return GetResponse.getResponse("Count all statuses successfully",
                materialService.countMaterialByStatus(eventId));
    }

    @PutMapping("/status/{materialId}")
    @Operation(summary = "Update material status by id")
    public ResponseEntity<?> updateMaterialStatus(
            @PathVariable @Positive @NotNull Integer materialId,
            @RequestParam  Status status
    ) {
        materialService.updateMaterialStatus(materialId, status);
        return UpdateResponse.updateResponse("Update status to " + status + " successfully",
              null );
    }

    @DeleteMapping("/delete/{materialId}")
    @Operation(summary = "Delete material by id")
    public ResponseEntity<?> deleteMaterialById(@PathVariable @Positive @NotNull Integer materialId) {
        materialService.deleteMaterialById(materialId);
        return GetResponse.getResponse("Delete material by id " + materialId + " successfully", null);
    }

    @DeleteMapping("/deletes")
    @Operation(summary = "Delete materials by ids")
    public ResponseEntity<?> deletesMaterialByIds(@RequestBody @Valid MultipleDelete materialIds) {
        materialService.deleteMaterialByIds(materialIds);
        return GetResponse.getResponse("Delete materials by id list successfully", null);
    }

    @PostMapping("/search")
    @Operation(summary = "Search materials by name")
    public ResponseEntity<?> SearchMaterialByName(
            @RequestParam @NotNull String materialName,
            @RequestParam @NotNull Integer eventId

    ) {
        return PostResponse.postResponse("Search material by name successfully",
                materialService.SearchMaterialByName(materialName, eventId));
    }

    @PutMapping("/handler/{materialId}")
    @Operation(summary = "Update handler by material id")
    public ResponseEntity<?> updateHandlerByMaterialId(
            @PathVariable @Positive @NotNull Integer materialId,
            @RequestParam @Positive @NotNull Integer handlerId
    ) {
        materialService.updateHandlerByMaterialId(materialId, handlerId);
        return UpdateResponse.updateResponse("Update handler successfully",
                null );
    }

    @PutMapping("/supporters/{materialId}")
    @Operation(summary = "Update supporters by material id")
    public ResponseEntity<?> updateSupportersByMaterialId(
            @PathVariable @Positive @NotNull Integer materialId,
            @RequestBody Supporter supporter
            ) {
        return UpdateResponse.updateResponse("Update supporters successfully",
                materialService.updateSupportersByMaterialId(materialId, supporter) );
    }

//    @PostMapping("/create")
//    @Operation(summary = "Create material")
//    public ResponseEntity<?> createMaterial(
//            @RequestBody MaterialRequest materialRequest,
//            @RequestBody AssetForCreateMaterial assetForCreateMaterial
//
//    ) {
//        return PostResponse.postResponse("create material successfully",
//                materialService.createMaterial(materialRequest, assetForCreateMaterial));
//    }
}
