package com.example.final_project.service.Impl;

import com.example.final_project.exception.BadRequestException;
import com.example.final_project.exception.NotFoundException;
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
    public MaterialResponse createMaterial(MaterialRequestForCreating materialRequest) {

        if(materialRequest.getHandlerId() == null)
            materialRequest.setHandlerId(null);
        else if(memberRepository.getMemberByMemberId(materialRequest.getHandlerId(), Token.getOrgIdByToken()) == null)
            throw new NotFoundException("Member id : " + materialRequest.getHandlerId() + " Not found");

        // check event id exists or not
        if(eventRepository.getEventById(Token.getOrgIdByToken(), materialRequest.getEventId()) == null)
            throw new NotFoundException("Event id : " + materialRequest.getEventId() + " Not found");

        // create material
        return modelMapper.map(materialRepository.createMaterial(materialRequest), MaterialResponse.class);
    }

    @Override
    public void createMultipleMaterials(List<MaterialRequestForMultiCreate> materialRequestForMultiCreateList) {
        MaterialRequestForCreating materialRequestForCreating;
        for(MaterialRequestForMultiCreate materialRequestForMultiCreate : materialRequestForMultiCreateList){
            materialRequestForCreating = modelMapper.map(materialRequestForMultiCreate, MaterialRequestForCreating.class);
            createMaterial(materialRequestForCreating);
        }
    }

    @Override
    public MaterialResponse updateMaterialDataByMaterialId(Integer materialId, MaterialRequestForUpdating materialRequestForUpdating) {

        // check event id exist or not
        if(eventRepository.getEventById(Token.getOrgIdByToken(), materialRequestForUpdating.getEventId()) == null)
            throw new NotFoundException("Event id : " + materialRequestForUpdating.getEventId() + " not found");

        if(materialRepository.getMaterialIdByMaterialId(materialId, materialRequestForUpdating.getEventId()) == null)
            throw new NotFoundException("Material id : " + materialId + " not found");

        if(materialRequestForUpdating.getHandlerId() == null)
            materialRequestForUpdating.setHandlerId(null);

        // check permission, if role user and is not a handler, don't have permission to change status
        Integer memberId = Token.getMemberIdByToken();
        Member member = memberRepository.getMemberByMemberId(memberId, Token.getOrgIdByToken());
        if(member.getRole().equals(Roles.ROLE_USER)){
            Material materialResponse = materialRepository.getMaterialById(materialId);
            if(!Objects.equals(memberId, materialResponse.getHandlerId()))
                throw new BadRequestException("You don't have permission to change status except you are a handler");
        }

        // if handlerId is thrown, check it exists or not
        if(materialRequestForUpdating.getHandlerId() != null){
            // check handler id exists or not
            if(memberRepository.getMemberByMemberId(materialRequestForUpdating.getHandlerId(), Token.getOrgIdByToken()) == null)
                throw new NotFoundException("Handler id : " + materialRequestForUpdating.getHandlerId() + " not found");
        }

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