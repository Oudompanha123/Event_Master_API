package com.example.final_project.model.dto.request.authentication;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

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
    @Size(min = 8, max = 20)
    private String password;
    @NotBlank
    @NotNull
    @Size(min = 8, max = 20)
    private String confirmPassword;
    @NotBlank
    @NotNull
    @Email
    private String email;
}
