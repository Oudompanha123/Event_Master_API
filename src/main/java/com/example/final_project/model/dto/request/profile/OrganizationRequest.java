package com.example.final_project.model.dto.request.profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationRequest {
    @NotNull
    @NotBlank
    private String orgName;
    @NotNull
    @NotBlank
    private String address;
    @NotNull
    @NotBlank
    private String logo;
}
