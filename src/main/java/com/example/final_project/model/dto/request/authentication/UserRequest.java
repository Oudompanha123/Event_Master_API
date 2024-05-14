package com.example.final_project.model.dto.request.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @NotBlank
    @NotNull
    private String userName;
    @NotBlank
    @NotNull
    private String phone;
    @NotBlank
    @NotNull
    private String password;
    @NotBlank
    @NotNull
    private String confirmPassword;
    @NotBlank
    @NotNull
    private String role;
    @NotBlank
    @NotNull
    private String orgCode;
    @NotBlank
    @NotNull
    @Email
    private String email;
}
