package com.example.final_project.service.Impl;

import com.example.final_project.exception.NotFoundException;
import com.example.final_project.model.dto.response.landingPage.*;
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
    private  final LandingPageRepository landingPageRepository;

    @Override
    public List<EventsByCategory> getAllEventsByCategory() {
        List<AllFieldInLandingPage> allFieldInLandingPages = landingPageRepository.getAllEventsByCategory();

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
    public EventDetailInLandingPage getDetailEventByEventId(Integer eventId) {
        // check event is existed or not
        if(landingPageRepository.getDetailEventByEventId(eventId) == null)
            throw new NotFoundException("Event id : " + eventId + " not found");
        return landingPageRepository.getDetailEventByEventId(eventId);
    }

    @Override
    public FormResponse getFormByEventId(Integer eventId) {
        if(landingPageRepository.getFormByEventId(eventId) == null)
            throw new NotFoundException("Event id : " + eventId + " don't have form");
        return landingPageRepository.getFormByEventId(eventId);
    }

    @Override
    public List<EventsByCategory> searchEvent(String eventName, Integer categoryId, Boolean status, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        List<AllFieldInLandingPage> allFieldInLandingPages = landingPageRepository.searchEvent(eventName, categoryId, status, startDateTime, endDateTime);
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
        return landingPageRepository.getAllPopularEvent();
    }

    @Override
    public EventsByCategory getEventByCategoryName(String cateName) {
        List<EventsByCategory> eventsByCategories = getAllEventsByCategory();
        for(EventsByCategory eventsByCategory : eventsByCategories){
            if(eventsByCategory.getCateName().equalsIgnoreCase(cateName)){
                return eventsByCategory;
            }
        }
        throw new NotFoundException("Category name : " + cateName + " not found");
    }
}
