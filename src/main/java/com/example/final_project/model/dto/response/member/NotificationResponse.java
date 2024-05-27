package com.example.final_project.model.dto.response.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {
    private Integer memberId;
    private String memberName;
    private String gender;
    private String email;
    private String picture;
    private String phone;
}
