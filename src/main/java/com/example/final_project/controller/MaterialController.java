package com.example.final_project.controller;

import com.example.final_project.model.constant.Status;
import com.example.final_project.model.dto.request.material.MultipleDelete;
import com.example.final_project.model.dto.response.GetResponse;
import com.example.final_project.service.MaterialService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/material")
@SecurityRequirement(name = "bearerAuth")
public class MaterialController {
    private final MaterialService materialService;

    @GetMapping
    public ResponseEntity<?> getAllMaterials() {
        return GetResponse.getResponse("Get all materials successfully.", materialService.findAllMaterial());
    }

    @GetMapping("/count-status/{eventId}")
    public ResponseEntity<?> getAllMaterialsCount(@PathVariable Integer eventId ) {
        return GetResponse.getResponse("Count all statuses successfully.",
                materialService.countMaterialByStatus(eventId));
    }

    @PutMapping("/status/{materialId}")
    public ResponseEntity<?> updateMaterialStatus(@PathVariable Integer materialId, @RequestParam Status status) {
        return GetResponse.getResponse("Update status to " + status + " successfully.",materialService.updateMaterialStatus(materialId, status));
    }

    @DeleteMapping("/delete/{materialId}")
    public ResponseEntity<?> deleteMaterialById(@PathVariable Integer materialId) {
        materialService.deleteMaterialById(materialId);
        return GetResponse.getResponse("Delete material by id " + materialId + " successfully.", null);
    }

    @DeleteMapping("/deletes")
    public ResponseEntity<?> deletesMaterialByIds(@RequestBody MultipleDelete materialIds) {
        materialService.deleteMaterialByIds(materialIds);
        return GetResponse.getResponse("Delete material by id " + materialIds + " successfully.", null);
    }

}
