package com.example.final_project.service;

import com.example.final_project.model.Organization;
import com.example.final_project.model.dto.request.profile.ChangePasswordRequest;
import com.example.final_project.model.dto.request.profile.MemberRequest;
import com.example.final_project.model.dto.request.profile.OrganizationRequest;
import com.example.final_project.model.dto.response.profile.MemberProfileResponse;

public interface ProfileService {
    MemberProfileResponse findProfileMember();

    MemberProfileResponse updateProfile(Integer profileId, MemberRequest memberRequest);

    Organization findProfileOrganization();

    Organization updateProfileOrganization(Integer orgId, OrganizationRequest organizationRequest);

    String changePassword(ChangePasswordRequest changePasswordRequest);

}
