package com.example.final_project.service;

import com.example.final_project.model.Event;
import com.example.final_project.model.constant.Active;
import com.example.final_project.model.dto.request.event.EventRequest;
import com.example.final_project.model.dto.request.event.FormRequest;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    Integer getTotalEventRecords();

    List<Event> getAllEvents(Integer offset, Integer limit);

    Event getEventById(Integer eventId);

    Event createEvent(EventRequest eventRequest) throws JsonProcessingException;

    void deleteEventById(Integer eventId);

    Event updateEventById(EventRequest eventRequest, Integer eventId);

    void updateActiveById(Integer eventId, Active active);

    Integer getTotalEventRecordsFromSearch(String eventName, Integer categoryId, Boolean status, LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<Event> searchEvent(String eventName, Integer categoryId, Boolean status, LocalDateTime startDateTime, LocalDateTime endDateTime, Integer offset, Integer limit);

    Event modifyRegistrationForm(Integer eventId, FormRequest formRequest);
}
