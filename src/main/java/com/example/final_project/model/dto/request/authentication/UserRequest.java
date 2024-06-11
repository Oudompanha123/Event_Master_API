package com.example.final_project.model.dto.request.authentication;

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
    @Size(min = 8, message = "size must be greater than or equal 8")
    private String password;
    @NotBlank
    @NotNull
    @Size(min = 8, message = "size must be greater than or equal 8")
    private String confirmPassword;
    @NotBlank
    @NotNull
    @Size(min = 6, max = 6)
    private String orgCode;
    @NotBlank
    @NotNull
    @Size(min = 11, max = 40)
    @Email(message = "Invalid email format")
    private String email;
}
