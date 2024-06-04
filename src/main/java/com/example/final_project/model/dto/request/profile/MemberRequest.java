package com.example.final_project.model.dto.request.profile;

import com.fasterxml.jackson.annotation.JsonFormat;
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
