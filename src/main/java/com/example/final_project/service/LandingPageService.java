package com.example.final_project.service;

import com.example.final_project.model.dto.response.landingPage.*;

import java.time.LocalDateTime;
import java.util.List;

public interface LandingPageService {

    EventDetailInLandingPage getDetailEventByEventId(Integer eventId);

    FormResponse getFormByEventId(Integer eventId);

    List<EventsByCategory> searchEvent(String eventName, String categoryName, Boolean status, LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<PopularEventResponse> getAllPopularEvent();

    List<EventResponseLandingPage> getEventByCategoryName(String cateName, Integer offset, Integer limit);

    List<String> getAllCategoryNames();
}
