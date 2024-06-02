package com.example.final_project.service.Impl;

import com.example.final_project.model.Member;
import com.example.final_project.model.Organization;
import com.example.final_project.model.dto.request.profile.MemberRequest;
import com.example.final_project.model.dto.request.profile.OrganizationRequest;
import com.example.final_project.model.dto.response.profile.MemberProfileResponse;
import com.example.final_project.repository.ProfileRepository;
import com.example.final_project.service.ProfileService;
import com.example.final_project.util.Token;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;

    @Override
    public MemberProfileResponse findProfileMember() {
        return profileRepository.findProfile(Token.getMemberIdByToken(), Token.getOrgIdByToken());
    }

    @Override
    public Member updateProfile(Integer profileId, MemberRequest memberRequest) {
        return profileRepository.updateProfile(profileId, memberRequest, Token.getMemberIdByToken());
    }

    @Override
    public Organization findProfileOrganization() {
        return profileRepository.findProfileOrganization(Token.getOrgIdByToken());
    }

    @Override
    public Organization updateProfileOrganization(Integer orgId, OrganizationRequest organizationRequest) {
        return profileRepository.updateProfileOrganization(orgId, organizationRequest);
    }

}
