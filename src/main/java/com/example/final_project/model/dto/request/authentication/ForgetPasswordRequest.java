package com.example.final_project.model.dto.request.authentication;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForgetPasswordRequest {
    @NotBlank
    @NotNull
    @Size(min = 8, max = 20)
    private String password;
    @NotNull
    @NotBlank
    @Size(min = 8, max = 20)
    private String confirmPassword;
}
