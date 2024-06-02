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
    private String memberName;
    @NotNull
    @NotBlank
    @Pattern(regexp = "^(Fem|M)ale$")
    private String gender;
    @NotNull
    @NotBlank
    @Size(min = 9, max = 16)
    private String phone;
    @NotNull
    @NotBlank
    private String address;
    @NotNull
    @NotBlank
    private String picture;
    @NotNull
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDate dateOfBirth;
}
