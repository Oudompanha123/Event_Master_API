package com.example.final_project.service;

import com.example.final_project.model.dto.response.landingPage.EventDetailInLandingPage;
import com.example.final_project.model.dto.response.landingPage.EventsByCategory;
import com.example.final_project.model.dto.response.landingPage.FormResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface LandingPageService {
    List<EventsByCategory> getAllEventsByCategory();

    EventDetailInLandingPage getDetailEventByEventId(Integer eventId);

    FormResponse getFormByEventId(Integer eventId);

    List<EventsByCategory> searchEvent(String eventName, Integer categoryId, Boolean status, LocalDateTime startDateTime, LocalDateTime endDateTime);
}
