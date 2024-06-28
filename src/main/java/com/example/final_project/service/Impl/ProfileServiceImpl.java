package com.example.final_project.service.Impl;

import com.example.final_project.exception.BadRequestException;
import com.example.final_project.exception.NotFoundException;
import com.example.final_project.model.Member;
import com.example.final_project.model.Organization;
import com.example.final_project.model.dto.request.profile.ChangePasswordRequest;
import com.example.final_project.model.dto.request.profile.MemberRequest;
import com.example.final_project.model.dto.request.profile.OrganizationRequest;
import com.example.final_project.model.dto.response.profile.MemberProfileResponse;
import com.example.final_project.repository.MemberRepository;
import com.example.final_project.repository.ProfileRepository;
import com.example.final_project.service.ProfileService;
import com.example.final_project.util.Token;
import com.example.final_project.util.Validation;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public MemberProfileResponse findProfileMember() {
        return profileRepository.findProfile(Token.getMemberIdByToken(), Token.getOrgIdByToken());
    }

    @Override
    public MemberProfileResponse updateProfile(Integer profileId, MemberRequest memberRequest) {
        if(memberRepository.getMemberByMemberId(profileId, Token.getOrgIdByToken()) == null)
            throw new NotFoundException("Member id : " + profileId + " not found");
        Validation.validatePhoneNumber(memberRequest.getPhone());
        return profileRepository.updateProfile(profileId, memberRequest);
    }

    @Override
    public Organization findProfileOrganization() {
        return profileRepository.findProfileOrganization(Token.getOrgIdByToken());
    }

    @Override
    public Organization updateProfileOrganization(Integer orgId, OrganizationRequest organizationRequest) {
        if(memberRepository.getOrganizationById(orgId) == null)
            throw new NotFoundException("Organization id : " + orgId + " not found");
        return profileRepository.updateProfileOrganization(orgId, organizationRequest);
    }

    @Override
    public String changePassword(ChangePasswordRequest changePasswordRequest) {
        Member member = memberRepository.findByEmail(Token.getEmailByToken());
        if (member == null)
            throw new BadRequestException("Member not found");

        // check old password match or not
        if(!bCryptPasswordEncoder.matches(changePasswordRequest.getOldPassword(), member.getPassword()))
            throw new BadRequestException("Old password does not match");

        // check password and confirm password match or not
        if(!changePasswordRequest.getConfirmPassword().equals(changePasswordRequest.getPassword()))
            throw new BadRequestException("Password and Confirm password do not matched");

        // new password to the member
        changePasswordRequest.setPassword(bCryptPasswordEncoder.encode(changePasswordRequest.getConfirmPassword()));
        memberRepository.changePassword(Token.getEmailByToken(), changePasswordRequest);

        return "Your password is changed successfully";
    }

}
