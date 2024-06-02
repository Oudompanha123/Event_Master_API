package com.example.final_project.model.dto.request.profile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
    @NotBlank
    @NotNull
//    @Min(value = 8)
    private String oldPassword;
    @NotBlank
    @NotNull
//    @Min(value = 8)
    private String password;
    @NotNull
    @NotBlank
//    @Min(value = 8)
    private String confirmPassword;
}
