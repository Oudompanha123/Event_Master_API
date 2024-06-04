package com.example.final_project.model.dto.request.authentication;

import jakarta.validation.constraints.*;
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
    @Min(value = 8)
    private String password;
    @NotBlank
    @NotNull
    @Min(value = 8)
    private String confirmPassword;
    @NotBlank
    @NotNull
    @Email
    private String email;
}
