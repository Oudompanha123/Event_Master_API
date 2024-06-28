package com.example.final_project.service.Impl;

import com.example.final_project.exception.BadRequestException;
import com.example.final_project.exception.NotFoundException;
import com.example.final_project.model.dto.response.landingPage.*;
import com.example.final_project.repository.EventRepository;
import com.example.final_project.repository.LandingPageRepository;
import com.example.final_project.service.LandingPageService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


@Service
@AllArgsConstructor
public class LandingPageServiceImpl implements LandingPageService {
    private final ModelMapper modelMapper;
    private final LandingPageRepository landingPageRepository;
    private final EventRepository eventRepository;

    @Override
    public EventDetailInLandingPage getDetailEventByEventId(Integer eventId) {
        // check event is existed or not
        if(landingPageRepository.getDetailEventByEventId(eventId) == null)
            throw new NotFoundException("Event id : " + eventId + " not found");
        return landingPageRepository.getDetailEventByEventId(eventId);
    }

    @Override
    public FormResponse getFormByEventId(Integer eventId) {

        // check event is_post = true or not
        if(landingPageRepository.getEventById(eventId) == null)
            throw new BadRequestException("Event id : " + eventId + " is not published");

        // check form when it is null
        if(landingPageRepository.getFormByEventId(eventId) == null)
            throw new NotFoundException("Event id : " + eventId + " don't have form");

        // check attendee is full or not
        Integer maxAttendees = eventRepository.getEventByIdNoOrgId(eventId).getMaxAttendee();
        Integer totalAttendeesInEvent = landingPageRepository.getTotalAttendees(eventId);
        if(totalAttendeesInEvent >= maxAttendees)
            throw new BadRequestException("Attendee in event is full");

        return landingPageRepository.getFormByEventId(eventId);
    }

    @Override
    public List<EventsByCategory> searchEvent(String eventName, String cateName, Boolean status, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<AllFieldInLandingPage> allFieldInLandingPages = landingPageRepository.searchEvent(eventName, cateName, status, startDateTime, endDateTime);
        List<String> allCateNames = new ArrayList<>();
        for (AllFieldInLandingPage obj : allFieldInLandingPages){
            allCateNames.add(obj.getCateName());
        }
        // Using HashSet to remove duplicates
        HashSet<String> uniqueSet = new HashSet<>(allCateNames);

        // Creating a new ArrayList from the unique elements
        allCateNames = new ArrayList<>(uniqueSet);

        // data response to clients
        List<EventsByCategory> eventsByCategories = new ArrayList<>();

        for(String categoryName : allCateNames){
            EventsByCategory eventsByCategory = new EventsByCategory();
            eventsByCategory.setCateName(categoryName);
            List<AllFieldInLandingPage> filteredList = allFieldInLandingPages.stream()
                    .filter(item -> item.getCateName().equals(categoryName))
                    .toList();

            // convert all object in AllFieldInLandingPage to EventResponseLandingPage
            List<EventResponseLandingPage> events = new ArrayList<>();
            for(AllFieldInLandingPage item : filteredList){
                EventResponseLandingPage e = modelMapper.map(item, EventResponseLandingPage.class);
                events.add(e);
            }
            eventsByCategory.setEvents(events);
            eventsByCategories.add(eventsByCategory);
        }
        return eventsByCategories;
    }

    @Override
    public List<PopularEventResponse> getAllPopularEvent() {
        Integer maxAttendee = landingPageRepository.getMaximumAttendeeInEvents();
        // calculate to find 60% of maxAttendee
        Double attendees = Math.ceil(maxAttendee * 0.6);
        return landingPageRepository.getAllPopularEvent(attendees);
    }

    @Override
    public List<EventResponseLandingPage> getEventByCategoryName(String cateName, Integer offset, Integer limit) {
        offset = (offset - 1) * limit;
        if(landingPageRepository.getEventByCategoryName(cateName, offset, limit).isEmpty())
            throw new NotFoundException("Category name : " + cateName + " not found");

        return landingPageRepository.getEventByCategoryName(cateName, offset, limit);
    }

    @Override
    public List<String> getAllCategoryNames() {
        return landingPageRepository.getAllCategoryNames();
    }
}
