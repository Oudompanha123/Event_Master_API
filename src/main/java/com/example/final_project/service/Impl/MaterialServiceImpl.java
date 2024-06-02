package com.example.final_project.service.Impl;

import com.example.final_project.model.MaterialStatusCount;
import com.example.final_project.model.constant.Status;
import com.example.final_project.model.dto.request.material.MultipleDelete;
import com.example.final_project.model.dto.response.material.MaterialResponse;
import com.example.final_project.repository.MaterialRepository;
import com.example.final_project.service.MaterialService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MaterialServiceImpl implements MaterialService {
    private final MaterialRepository materialRepository;

    @Override
    public List<MaterialResponse> findAllMaterial() {
        return materialRepository.getAllMaterial();
    }

    @Override
    public List<MaterialStatusCount> countMaterialByStatus(Integer eventId) {
        return materialRepository.getMaterialStatusCount(eventId);
    }

    @Override
    public MaterialResponse updateMaterialStatus(Integer materialId, Status status) {
        return materialRepository.updateMaterialStatus(materialId, status);
    }

    @Override
    public void deleteMaterialById(Integer materialId) {
        materialRepository.deleteMaterialById(materialId);
    }

    @Override
    public void deleteMaterialByIds(MultipleDelete materialIds) {
        for (Integer materialId : materialIds.getMaterialIds()) {
            materialRepository.deleteMaterialById(materialId);
        }
        materialRepository.deleteMaterialByIds(materialIds);
    }

    @Override
    public Integer totalMaterial() {
        int totalMaterial = 0;
        return materialRepository.totalMaterial(totalMaterial);
    }

}