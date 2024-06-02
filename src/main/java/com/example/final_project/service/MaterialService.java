package com.example.final_project.service;

import com.example.final_project.model.MaterialStatusCount;
import com.example.final_project.model.constant.Status;
import com.example.final_project.model.dto.request.material.MultipleDelete;
import com.example.final_project.model.dto.response.material.MaterialResponse;

import java.util.List;

public interface MaterialService {
    List<MaterialResponse> findAllMaterial();

    List<MaterialStatusCount> countMaterialByStatus(Integer eventId);

    MaterialResponse updateMaterialStatus(Integer materialId, Status statusId);

    void deleteMaterialById(Integer materialId);

    void deleteMaterialByIds(MultipleDelete materialIds);

    Integer totalMaterial();
}