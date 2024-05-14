package com.example.final_project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Organization {
    private Integer orgId;
    private String orgName;
    private String code;
    private String address;
    private String logo;
}
