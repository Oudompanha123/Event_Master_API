package com.example.final_project.model.dto.request.authentication;

import com.example.final_project.model.constant.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminRequest {
    @NotBlank
    @NotNull
    private String adminName;
    @NotBlank
    @NotNull
    private String phone;
    @NotBlank
    @NotNull
    private String password;
    @NotBlank
    @NotNull
    private String confirmPassword;
    Roles role;
    @NotBlank
    @NotNull
    @Email
    private String email;
}
