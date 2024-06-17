package com.example.final_project.service.Impl;

import com.example.final_project.exception.BadRequestException;
import com.example.final_project.exception.NotFoundException;
import com.example.final_project.model.Event;
import com.example.final_project.model.constant.Active;
import com.example.final_project.model.dto.request.event.EventRequest;
import com.example.final_project.model.dto.request.event.FormRequest;
import com.example.final_project.repository.CategoryRepository;
import com.example.final_project.repository.EventRepository;
import com.example.final_project.service.EventService;
import com.example.final_project.util.RegistrationFormString;
import com.example.final_project.util.Token;
import com.example.final_project.util.Validation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Integer getTotalEventRecords() {
        Integer orgId = Token.getOrgIdByToken();
        return eventRepository.getTotalEventRecords(orgId);
    }

    @Override
    public List<Event> getAllEvents(Integer offset, Integer limit) {
        Integer orgId = Token.getOrgIdByToken();
        offset = (offset - 1) * limit;
        return eventRepository.getAllEvents(orgId, offset, limit);
    }

    @Override
    public Event getEventById(Integer eventId) {
        // check event is existed or not
        if(eventRepository.getEventById(Token.getOrgIdByToken(), eventId) == null)
            throw new NotFoundException("Event id : " + eventId + " not found");
        return eventRepository.getEventById(Token.getOrgIdByToken(), eventId);
    }

    @Override
    public Event createEvent(EventRequest eventRequest){

        // check image format
        Validation.validateImage(eventRequest.getPoster());
        
        // start date must be later than now
        if(eventRequest.getStartDate().isBefore(LocalDateTime.now()))
            throw new BadRequestException("Start date must be later than now");

        // check start date must be earlier than end date
        if(eventRequest.getStartDate().isAfter(eventRequest.getEndDate()))
            throw new BadRequestException("Start date must be earlier than end date");

        // check categoryId in EventRequest has been found in database or not
        if(categoryRepository.getCategoryById(eventRequest.getCategoryId(), Token.getOrgIdByToken()) == null)
            throw new NotFoundException("Category id : " + eventRequest.getCategoryId() + " not found");

        // set default form to event by category name
        String cateName = categoryRepository.getCategoryById(eventRequest.getCategoryId(), Token.getOrgIdByToken()).getCategoryName();
        if(cateName.equals("Conferences")){
            eventRequest.setDataJsonString(RegistrationFormString.getConferenceString());
        } else if (cateName.equals("Marathons And Races")) {
            eventRequest.setDataJsonString(RegistrationFormString.getMarathonString());
        }
        else {
            eventRequest.setDataJsonString(RegistrationFormString.getUnknownCategoryString());
        }

        return eventRepository.createEvent(Token.getOrgIdByToken(), eventRequest);
    }

    @Override
    public void deleteEventById(Integer eventId) {
        // check event is existed or not
        Event event = eventRepository.getEventById(Token.getOrgIdByToken(), eventId);
        if(event == null)
            throw new NotFoundException("Event id : " + eventId + " not found");
        eventRepository.deleteEventById(Token.getOrgIdByToken(), eventId);
    }

    @Override
    public Event updateEventById(EventRequest eventRequest, Integer eventId) {
        // check event is existed or not
        Event event = eventRepository.getEventById(Token.getOrgIdByToken(), eventId);
        if(event == null)
            throw new NotFoundException("Event id : " + eventId + " not found");
        // check categoryId in EventRequest has been found in database or not
        if(categoryRepository.getCategoryById(eventRequest.getCategoryId(), Token.getOrgIdByToken()) == null)
            throw new NotFoundException("Category id : " + eventRequest.getCategoryId() + " not found");

        // check image format
        Validation.validateImage(eventRequest.getPoster());

        return eventRepository.updateEventById(eventRequest, eventId, Token.getOrgIdByToken());
    }

    @Override
    public void updateActiveById(Integer eventId, Active active) {
        // check event is existed or not
        Event event = eventRepository.getEventById(Token.getOrgIdByToken(), eventId);
        if(event == null)
            throw new NotFoundException("Event id : " + eventId + " not found");
        // casting active enum to boolean. (close = false and open = true)
        boolean isOpen = active == Active.open;
        eventRepository.updateActiveById(eventId, Token.getOrgIdByToken(), isOpen);
    }

    @Override
    public Integer getTotalEventRecordsFromSearch(String eventName, Integer categoryId, Boolean status, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if(eventName != null)
            eventName = eventName.trim();
        Integer orgId = Token.getOrgIdByToken();
        return eventRepository.getTotalEventRecordsFromSearch(eventName, categoryId, status, startDateTime, endDateTime, orgId);
    }

    @Override
    public List<Event> searchEvent(String eventName, Integer categoryId, Boolean status, LocalDateTime startDateTime, LocalDateTime endDateTime, Integer offset, Integer limit) {
        if(eventName != null)
            eventName = eventName.trim();
        Integer orgId = Token.getOrgIdByToken();
        offset = (offset - 1) * limit;
        return eventRepository.getSearchAllEvent(eventName, categoryId, status, startDateTime, endDateTime, orgId, offset, limit);
    }

    @Override
    public Event modifyRegistrationForm(Integer eventId, FormRequest formRequest) {
        // check event id in database or not
        Event event = eventRepository.getEventById(Token.getOrgIdByToken(), eventId);
        if(event == null)
            throw new NotFoundException("Event id : " + eventId + " not found");

        // clear all registration form data
        eventRepository.clearRegistrationFormById(eventId, Token.getOrgIdByToken());
        return eventRepository.modifyRegistrationForm(eventId, formRequest, Token.getOrgIdByToken());
    }
}
