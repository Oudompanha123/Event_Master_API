package com.example.final_project.service;

import com.example.final_project.model.MaterialStatusCount;
import com.example.final_project.model.Supporter;
import com.example.final_project.model.constant.Status;
import com.example.final_project.model.dto.request.material.MaterialRequestForCreating;
import com.example.final_project.model.dto.request.material.MaterialRequestForMultiCreate;
import com.example.final_project.model.dto.request.material.MultipleDelete;
import com.example.final_project.model.Material;
import com.example.final_project.model.dto.response.material.MaterialResponseForCreating;

import java.util.List;

public interface MaterialService {
    List<Material> getAllMaterials(Integer eventId);

    MaterialStatusCount countMaterialByStatus(Integer eventId);

    void updateMaterialStatus(Integer materialId, Status statusId);

    void deleteMaterialById(Integer materialId);

    void deleteMaterialByIds(MultipleDelete materialIds);

    Material getMaterialById(Integer materialId);

    List<Material> SearchMaterialByName(String materialName, Integer eventId);

    void updateHandlerByMaterialId(Integer materialId, Integer handlerId);

    Supporter updateSupportersByMaterialId(Integer materialId, Supporter supporter);

    MaterialResponseForCreating createMaterial(MaterialRequestForCreating materialRequest, Integer assetId);

    void createMultipleMaterials(List<MaterialRequestForMultiCreate> materialRequestForMultiCreateList);
}