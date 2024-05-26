package com.example.final_project.controller;

import com.example.final_project.model.constant.Active;
import com.example.final_project.model.dto.request.event.EventRequest;
import com.example.final_project.model.dto.request.event.FormRequest;
import com.example.final_project.model.dto.request.event.SearchEventRequest;
import com.example.final_project.model.dto.response.GetAllResponse;
import com.example.final_project.model.dto.response.GetResponse;
import com.example.final_project.model.dto.response.PostResponse;
import com.example.final_project.model.dto.response.UpdateResponse;
import com.example.final_project.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/event")
@SecurityRequirement(name = "bearerAuth")
@AllArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping
    @Operation(summary = "Get All Events")
    public ResponseEntity<?> getAllEvents(
            @RequestParam(defaultValue = "1") @Positive Integer offset,
            @RequestParam(defaultValue = "15") @Positive Integer limit
    ){
        return GetAllResponse.getAllResponse("Get All events successfully",
                eventService.getTotalEventRecords(), eventService.getAllEvents(offset, limit));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Event By Id")
    public ResponseEntity<?> getEventById(@PathVariable(name = "id") Integer eventId){
        return GetResponse.getResponse("Get event successfully", eventService.getEventById(eventId));
    }

    @PostMapping
    @Operation(summary = "Create new event")
    public ResponseEntity<?> createEvent(@RequestBody @Valid EventRequest eventRequest) throws Exception{
        return PostResponse.postResponse("Create event successfully", eventService.createEvent(eventRequest));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete event by id")
    public ResponseEntity<?> deleteEventById(@PathVariable(name = "id") Integer eventId){
        eventService.deleteEventById(eventId);
        return GetResponse.getResponse("Delete event id : " + eventId + "  successfully", null);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update event by id")
    public ResponseEntity<?> updateEventById(@PathVariable(name = "id") Integer
                eventId, @RequestBody @Valid EventRequest eventRequest){
        return UpdateResponse.updateResponse("Update event successfully",
                eventService.updateEventById(eventRequest, eventId));
    }

    @PutMapping("/active/{id}")
    @Operation(summary = "Toggle active status of the event between 'open' and 'closed' by id")
    public ResponseEntity<?>updateActiveById(@Positive @PathVariable (name = "id") Integer eventId,
                                             @RequestParam Active active){
        eventService.updateActiveById(eventId, active);
        return UpdateResponse.updateResponse("Update event id : " + eventId + " to '" + active + "' successfully", null);
    }

    @PostMapping("/search")
    @Operation(summary = "Search and filter")
    public ResponseEntity<?> searchEvent(
            @RequestParam(defaultValue = "1") @Positive Integer offset,
            @RequestParam(defaultValue = "15") @Positive Integer limit,
            @RequestBody SearchEventRequest searchEventRequest
            ){
        return GetAllResponse.getAllResponse("Search event successfully",
                eventService.getTotalEventRecordsFromSearch(searchEventRequest),
                eventService.searchEvent(searchEventRequest, offset, limit));
    }

    @PutMapping("/registration-form/{id}")
    @Operation(summary = "Insert, delete and update registration form")
    public ResponseEntity<?> modifyRegistrationForm(
            @PathVariable(name = "id") Integer eventId,
            @RequestBody FormRequest formRequest
            ){
        return GetResponse.getResponse("Registration form is successfully modified",
                eventService.modifyRegistrationForm(eventId, formRequest));
    }
}
