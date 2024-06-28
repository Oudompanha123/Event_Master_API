package com.example.final_project.controller;

import com.example.final_project.model.dto.request.profile.ChangePasswordRequest;
import com.example.final_project.model.dto.request.profile.MemberRequest;
import com.example.final_project.model.dto.request.profile.OrganizationRequest;
import com.example.final_project.model.dto.response.GetResponse;
import com.example.final_project.model.dto.response.UpdateResponse;
import com.example.final_project.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@AllArgsConstructor
@RequestMapping("/api/profiles")
@SecurityRequirement(name = "bearerAuth")
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping
    @Operation(summary = "Get member profile ")
    public ResponseEntity<?> getProfile() {
        return GetResponse.getResponse("Get profile member successfully", profileService.findProfileMember());
    }

    @PutMapping("/update-member/{profileId}")
    @Operation(summary = "Update profile by id")
    public ResponseEntity<?> updateProfile(
            @PathVariable @Positive @NotNull Integer profileId,
            @RequestBody @Valid MemberRequest memberRequest
    ){
        return GetResponse.getResponse("Update profile by id " + profileId + " successfully.",
                profileService.updateProfile(profileId, memberRequest));
    }

    @GetMapping("/organization")
    @Operation(summary = "Get profile organization")
    public ResponseEntity<?> getOrganization(){
        return GetResponse.getResponse("Get profile organization.", profileService.findProfileOrganization());
    }

    @PutMapping("/update-organization/{orgId}")
    @Operation(summary = "Update organization by id")
    public ResponseEntity<?> updateOrganization(
            @PathVariable @Positive @NotNull Integer orgId,
            @RequestBody @Valid OrganizationRequest organizationRequest
    ){
        return GetResponse.getResponse("Update organization by id " + orgId + " successfully.",
                profileService.updateProfileOrganization(orgId, organizationRequest));
    }
    @PutMapping("/change-password")
    @Operation(summary = "Change password")
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest ){
        return UpdateResponse.updateResponse(profileService.changePassword(changePasswordRequest),null);
    }
}
