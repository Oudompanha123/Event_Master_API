package com.example.final_project.model.dto.response.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
    private Integer memberId;
    private String memberName;
    private String phone;
    private String email;
    private Boolean isVerify;
}
