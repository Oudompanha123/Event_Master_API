package com.example.final_project.service.Impl;

import com.example.final_project.exception.BadRequestException;
import com.example.final_project.exception.NotFoundException;
import com.example.final_project.model.Asset;
import com.example.final_project.model.MaterialStatusCount;
import com.example.final_project.model.Member;
import com.example.final_project.model.constant.Roles;
import com.example.final_project.model.constant.Status;
import com.example.final_project.model.dto.request.material.MaterialRequestForCreating;
import com.example.final_project.model.dto.request.material.MaterialRequestForMultiCreate;
import com.example.final_project.model.dto.request.material.MaterialRequestForUpdating;
import com.example.final_project.model.dto.request.material.MultipleDelete;
import com.example.final_project.model.Material;
import com.example.final_project.model.dto.response.material.MaterialResponse;
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
import java.util.Objects;

@Service
@AllArgsConstructor
public class MaterialServiceImpl implements MaterialService {
    private final MaterialRepository materialRepository;
    private final EventRepository eventRepository;
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    private final AssetRepository assetRepository;

    @Override
    public List<Material> getAllMaterials(Integer eventId) {
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
    public Material getMaterialById(Integer materialId) {
        if(materialRepository.getMaterialById(materialId) == null)
            throw new NotFoundException("Material id : " + materialId + " not found");
        return materialRepository.getMaterialById(materialId);
    }

    @Override
    public List<Material> SearchMaterialByName(String materialName, Integer eventId) {
        if(eventRepository.getEventById(Token.getOrgIdByToken(), eventId) == null)
            throw new NotFoundException("Event id : " + eventId + " not found");
        return materialRepository.searchMaterialByName(materialName, eventId);
    }

    @Override
    public MaterialResponse createMaterial(MaterialRequestForCreating materialRequest, Integer assetId) {

        if(materialRequest.getHandlerId() == null)
            materialRequest.setHandlerId(null);

        // check event id exists or not
        if(eventRepository.getEventById(Token.getOrgIdByToken(), materialRequest.getEventId()) == null)
            throw new NotFoundException("Event id : " + materialRequest.getEventId() + " Not found");

        // check valid status value (Issue, Done, OnGoing, Pending)
//        if(!materialRequest.getStatus().equals("Issue")
//            || !materialRequest.getStatus().equals("Done")
//            || !materialRequest.getStatus().equals("OnGoing")
//            || !materialRequest.getStatus().equals("Pending")
//        )
//            throw new BadRequestException("Invalid status : " + materialRequest.getStatus() + ". Correct values: Issue, Done, OnGoing, Pending ");

        // check toGet must be <= desire material qty that use in event
        if(materialRequest.getToGet() > materialRequest.getQty())
            throw new BadRequestException("The toGet cannot be greater than material qty that use in event");

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

                // if desire material qty in event is greater than asset qty, update new asset qty new 0
                float newAssetQty;
                if(asset.getQty() - materialRequest.getQty() < 0f) {
                    newAssetQty = 0f;
                    if (materialRequest.getToGet() != materialRequest.getQty() - asset.getQty())
                        throw new BadRequestException("toGet must be equal to " + (materialRequest.getQty() - asset.getQty()));

                }else{
                    // if asset is enough to create material, that means
                    // don't need to buy more material so (toGet = 0)
                    if(materialRequest.getToGet() != 0)
                        throw new BadRequestException("toGet must be equal to 0 because asset qty is enough to create material");
                    newAssetQty = asset.getQty() - materialRequest.getQty();
                }

                // update asset qty
                assetRepository.updateAssetQty(assetId, Token.getOrgIdByToken(), newAssetQty);
            }
        }

        // create material
        return modelMapper.map(materialRepository.createMaterial(materialRequest), MaterialResponse.class);
    }

    @Override
    public void createMultipleMaterials(List<MaterialRequestForMultiCreate> materialRequestForMultiCreateList) {
        MaterialRequestForCreating materialRequestForCreating;
        for(MaterialRequestForMultiCreate materialRequestForMultiCreate : materialRequestForMultiCreateList){
            materialRequestForCreating = modelMapper.map(materialRequestForMultiCreate, MaterialRequestForCreating.class);
            createMaterial(materialRequestForCreating, materialRequestForMultiCreate.getAssetId());
        }
    }

    @Override
    public MaterialResponse updateMaterialDataByMaterialId(Integer materialId, MaterialRequestForUpdating materialRequestForUpdating) {

//        if(materialRepository.getMaterialById(materialId) == null)
//            throw new NotFoundException("Material id : " + materialId + " not found");

        if(materialRequestForUpdating.getHandlerId() == null)
            materialRequestForUpdating.setHandlerId(null);

        // check permission, if role user and is not a handler, don't have permission to change status
        Integer memberId = Token.getMemberIdByToken();
        Member member = memberRepository.getMemberByMemberId(memberId, Token.getOrgIdByToken());
        if(member.getRole().equals(Roles.ROLE_USER)){
            Material materialResponse = materialRepository.getMaterialById(materialId);
            if(!Objects.equals(memberId, materialResponse.getHandlerId()))
                throw new BadRequestException("You don't have permission to change status even if You are a handler");
        }

        // check handler id exists or not
        if(memberRepository.getMemberByMemberId(materialRequestForUpdating.getHandlerId(), Token.getOrgIdByToken()) == null)
            throw new NotFoundException("Handler id : " + materialRequestForUpdating.getHandlerId() + " not found");

        // check toGet must be <= desire material qty that use in event
        if(materialRequestForUpdating.getToGet() > materialRequestForUpdating.getQty())
            throw new BadRequestException("The toGet cannot be greater than material qty that use in event");

        return materialRepository.updateMaterialDataByMaterialId(materialId, materialRequestForUpdating);
    }

    @Override
    public void updateMaterialStatus(Integer materialId, Status status) {

        // check permission, if role user and is not a handler, don't have permission to change status
        Integer memberId = Token.getMemberIdByToken();
        Member member = memberRepository.getMemberByMemberId(memberId, Token.getOrgIdByToken());
        if(member.getRole().equals(Roles.ROLE_USER)){
            Material materialResponse = materialRepository.getMaterialById(materialId);
            if(!Objects.equals(memberId, materialResponse.getHandlerId()))
                throw new BadRequestException("You don't have permission to change status even if You are a handler");
        }

        if(materialRepository.getMaterialById(materialId) == null)
            throw new NotFoundException("Material id : " + materialId + " not found");
        materialRepository.updateMaterialStatus(materialId, status);
    }
}