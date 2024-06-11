package com.example.final_project.model.dto.response.landingPage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PopularEventResponse {
    private Integer eventId;
    private String eventName;
    private String poster;
    private String description;
    private Integer attendee;
}
