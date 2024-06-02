package com.example.final_project.model.dto.response.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberProfileResponse {
    private Integer memberId;
    private String memberName;
    private String gender;
    private String email;
    private String address;
    private String picture;
    private String phone;
    private String dateOfBirth;
}
