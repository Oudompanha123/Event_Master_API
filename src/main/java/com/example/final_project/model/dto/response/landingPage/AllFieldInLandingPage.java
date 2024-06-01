package com.example.final_project.model.dto.response.landingPage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllFieldInLandingPage {
    private String cateName;
    private Integer eventId;
    private String eventName;
    private String description;
    private LocalDateTime startDate;
    private String address;
    private String poster;
    private Boolean isOpen;
    private String orgName;
    private String logo;
}
