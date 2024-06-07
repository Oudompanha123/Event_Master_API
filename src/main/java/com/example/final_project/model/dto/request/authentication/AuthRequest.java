package com.example.final_project.model.dto.request.authentication;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    @NotNull
    @NotBlank
    @Email
    private String email;
    @NotNull
    @NotBlank
    @Size(min = 8, max = 20)
    private String password;
}
