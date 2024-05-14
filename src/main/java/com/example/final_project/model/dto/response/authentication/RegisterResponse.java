package com.example.final_project.model.dto.response.authentication;

import com.example.final_project.model.Organization;
import lombok.Data;

@Data
public class RegisterResponse {
    private Integer memberId;
    private String memberName;
    private String phone;
    private String email;
    private String role;
    private Boolean isVerify;
    private Organization organization;
}
