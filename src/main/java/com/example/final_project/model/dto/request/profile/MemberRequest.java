package com.example.final_project.model.dto.request.profile;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequest {
    @NotNull
    @NotBlank
    @Size(min = 1, max = 40)
    private String memberName;
    @NotNull
    @NotBlank
    @Pattern(regexp = "^(Fem|M)ale$")
    private String gender;
    @NotNull
    @NotBlank
    private String phone;
    @NotNull
    @NotBlank
    private String address;
    @NotNull
    @NotBlank
    private String picture;
    private LocalDate dateOfBirth;
}
