package com.example.final_project.model.dto.response.landingPage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventsByCategory {
    private String cateName;
    private List<EventResponseLandingPage> events;
}
