package com.example.final_project.model.dto.response.authentication;

import com.example.final_project.model.Organization;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminLoginResponse {
    private String token;
    private Organization organization;
}
