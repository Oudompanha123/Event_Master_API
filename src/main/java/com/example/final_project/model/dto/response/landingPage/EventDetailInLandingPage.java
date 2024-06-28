package com.example.final_project.model.dto.response.landingPage;

import com.example.final_project.model.dto.response.AgendaResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDetailInLandingPage {
    private Integer eventId;
    private String eventName;
    private String description;
    private String location;
    private LocalDateTime startDateTime;
    private String poster;
    private Boolean isOpen;
    private AgendaResponse agenda;
}
