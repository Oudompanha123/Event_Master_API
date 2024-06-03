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
    @Min(value = 9)
    @Pattern(regexp = "^0[0-9]", message = "Invalid phone number. The number start should 0")
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
    @Min(value = 6)
    private String orgCode;
    @NotBlank
    @NotNull
    @Email
    private String email;
}
