package com.example.final_project.service.Impl;

import com.example.final_project.exception.BadRequestException;
import com.example.final_project.exception.NotFoundException;
import com.example.final_project.model.Asset;
import com.example.final_project.model.MaterialStatusCount;
import com.example.final_project.model.Supporter;
import com.example.final_project.model.constant.Status;
import com.example.final_project.model.dto.request.material.MaterialRequest;
import com.example.final_project.model.dto.request.material.MaterialRequestForCreating;
import com.example.final_project.model.dto.request.material.MultipleDelete;
import com.example.final_project.model.dto.response.material.MaterialResponse;
import com.example.final_project.model.dto.response.material.MaterialResponseForCreating;
import com.example.final_project.repository.AssetRepository;
import com.example.final_project.repository.EventRepository;
import com.example.final_project.repository.MaterialRepository;
import com.example.final_project.repository.MemberRepository;
import com.example.final_project.service.MaterialService;
import com.example.final_project.util.Token;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class MaterialServiceImpl implements MaterialService {
    private final MaterialRepository materialRepository;
    private final EventRepository eventRepository;
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    private final AssetRepository assetRepository;

    @Override
    public List<MaterialResponse> getAllMaterials(Integer eventId) {
        if(eventRepository.getEventById(Token.getOrgIdByToken(), eventId) == null)
            throw new NotFoundException("Event id : " + eventId + " not found");

        List<Integer> eventIdList = materialRepository.getAllEventIdInMaterialTable();
        if(eventIdList.contains(eventId))
            return materialRepository.getAllMaterial(eventId);
        else
            throw new NotFoundException("This event id : " + eventId + " has no any material");
    }

    @Override
    public MaterialStatusCount countMaterialByStatus(Integer eventId) {
        return materialRepository.getMaterialStatusCount(eventId);
    }

    @Override
    public void updateMaterialStatus(Integer materialId, Status status) {
        if(materialRepository.getMaterialById(materialId) == null)
            throw new NotFoundException("Material id : " + materialId + " not found");
        materialRepository.updateMaterialStatus(materialId, status);
    }

    @Override
    public void deleteMaterialById(Integer materialId) {
        if(materialRepository.getMaterialById(materialId) == null)
            throw new NotFoundException("Material id : " + materialId + " not found");
        materialRepository.deleteMaterialById(materialId);
    }

    @Override
    public void deleteMaterialByIds(MultipleDelete materialIds) {
        // check empty data in list
        if(materialIds.getMaterialIds().isEmpty())
            throw new BadRequestException("Material cannot be empty");

        // all id that pass from client must be match all, else throw exception
        if(materialRepository.getMaterialByIds(materialIds) != materialIds.getMaterialIds().size())
            throw new NotFoundException("Material id is not found");
        materialRepository.deleteMaterialByIds(materialIds);
    }

    @Override
    public MaterialResponse getMaterialById(Integer materialId) {
        if(materialRepository.getMaterialById(materialId) == null)
            throw new NotFoundException("Material id : " + materialId + " not found");
        return materialRepository.getMaterialById(materialId);
    }

    @Override
    public List<MaterialResponse> SearchMaterialByName(String materialName, Integer eventId) {
        if(eventRepository.getEventById(Token.getOrgIdByToken(), eventId) == null)
            throw new NotFoundException("Event id : " + eventId + " not found");
        return materialRepository.searchMaterialByName(materialName, eventId);
    }

    @Override
    public void updateHandlerByMaterialId(Integer materialId, Integer handlerId) {
        if(materialRepository.getMaterialById(materialId) == null)
            throw new NotFoundException("Material id : " + materialId + " not found");

        if(memberRepository.getMemberByMemberId(handlerId) == null)
            throw new NotFoundException("Handler id : " + handlerId + " not found");

        materialRepository.updateHandlerByMaterialId(materialId, handlerId);
    }

    @Override
    public Supporter updateSupportersByMaterialId(Integer materialId, Supporter supporter) {
        // check material id exists or not
        if(materialRepository.getMaterialById(materialId) == null)
            throw new NotFoundException("Material id : " + materialId + " not found");

        return materialRepository.updateSupportersByMaterialId(materialId, supporter);
    }

    @Override
    public MaterialResponseForCreating createMaterial(MaterialRequestForCreating materialRequest, Integer assetId) {
        // check handler id exists or not
        if(memberRepository.getMemberByMemberId(materialRequest.getHandlerId()) == null)
            throw new NotFoundException("Handler id : " + materialRequest.getHandlerId() + " Not found");

        // check event id exists or not
        if(eventRepository.getEventById(Token.getOrgIdByToken(), materialRequest.getEventId()) == null)
            throw new NotFoundException("Event id : " + materialRequest.getEventId() + " Not found");

        // if asset id is passed, update asset qty
        if(assetId != null){
            // check asset id exists or not
            if(assetRepository.findAssetById(assetId, Token.getOrgIdByToken()) == null){
                throw new NotFoundException("Asset id : " + assetId + " Not found");
            }
            else{
                Asset asset = assetRepository.findAssetById(assetId, Token.getOrgIdByToken());

                // check asset unit is match with material unit or not
                if(!asset.getUnit().equalsIgnoreCase(materialRequest.getUnit()))
                    throw new BadRequestException("Asset id : " + assetId + ", unit is not match with material unit");

                // check asset name is match with material name or not
                if(!asset.getAssetName().equalsIgnoreCase(materialRequest.getMaterialName()))
                    throw new BadRequestException("Asset id : " + assetId + ", name is not match with material name");

                // check asset qty is enough to create material or not
                if(asset.getQty() < materialRequest.getQty()){
                    throw new BadRequestException("Asset id : " + assetId + ", qty is not enough to create material");
                }

                // update asset qty
                assetRepository.updateAssetQty(assetId, Token.getOrgIdByToken(), asset.getQty() - materialRequest.getQty());
            }
        }

        // create material
        return modelMapper.map(materialRepository.createMaterial(materialRequest), MaterialResponseForCreating.class);
    }

}