package com.example.final_project.model.dto.request.authentication;

import com.example.final_project.model.constant.Roles;
import jakarta.validation.constraints.*;
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
    @Min(value = 8)
    private String password;
    @NotBlank
    @NotNull
    @Min(value = 8)
    private String confirmPassword;
    @NotBlank
    @NotNull
    @Size(min = 6, max = 6)
    private String orgCode;
    @NotBlank
    @NotNull
    @Email
    private String email;
}
