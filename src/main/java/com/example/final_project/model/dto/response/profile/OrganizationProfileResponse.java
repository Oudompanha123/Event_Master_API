package com.example.final_project.model.dto.response.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationProfileResponse {
    private Integer orgId;
    private String orgName;
    private String code;
    private String address;
    private String logo;
}
