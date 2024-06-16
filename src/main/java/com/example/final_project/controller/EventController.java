package com.example.final_project.controller;

import com.example.final_project.model.constant.Active;
import com.example.final_project.model.dto.request.event.EventRequest;
import com.example.final_project.model.dto.request.event.FormRequest;
import com.example.final_project.model.dto.response.GetAllResponse;
import com.example.final_project.model.dto.response.GetResponse;
import com.example.final_project.model.dto.response.PostResponse;
import com.example.final_project.model.dto.response.UpdateResponse;
import com.example.final_project.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@AllArgsConstructor
@RequestMapping("/api/events")
@SecurityRequirement(name = "bearerAuth")
public class EventController {
    private final EventService eventService;

    @GetMapping
    @Operation(summary = "Get All Events")
    public ResponseEntity<?> getAllEvents(
            @RequestParam(defaultValue = "1") @Positive @NotNull Integer offset,
            @RequestParam(defaultValue = "15") @Positive @NotNull Integer limit
    ){
        return GetAllResponse.getAllResponse("Get all events successfully",
                eventService.getTotalEventRecords(), eventService.getAllEvents(offset, limit));
    }

    @GetMapping("/{eventId}")
    @Operation(summary = "Get Event By Id")
    public ResponseEntity<?> getEventById(@PathVariable @Positive @NotNull Integer eventId){
        return GetResponse.getResponse("Get event successfully", eventService.getEventById(eventId));
    }

    @PostMapping
    @Operation(summary = "Create new event")
    public ResponseEntity<?> createEvent(@RequestBody @Valid EventRequest eventRequest) throws Exception{
        return PostResponse.postResponse("Create event successfully", eventService.createEvent(eventRequest));
    }

    @DeleteMapping("/{eventId}")
    @Operation(summary = "Delete event by id")
    public ResponseEntity<?> deleteEventById(@PathVariable @Positive @NotNull Integer eventId){
        eventService.deleteEventById(eventId);
        return GetResponse.getResponse("Delete event id : " + eventId + "  successfully", null);
    }

    @PutMapping("/{eventId}")
    @Operation(summary = "Update event by id")
    public ResponseEntity<?> updateEventById(
            @PathVariable @Positive @NotNull Integer eventId,
            @RequestBody @Valid EventRequest eventRequest
    ){
        return UpdateResponse.updateResponse("Update event successfully",
                eventService.updateEventById(eventRequest, eventId));
    }

    @PutMapping("/active/{eventId}")
    @Operation(summary = "Toggle active status of the event between 'open' and 'closed' by id")
    public ResponseEntity<?>updateActiveById(
            @PathVariable @Positive @NotNull Integer eventId,
            @RequestParam @NotNull Active active
    ){
        eventService.updateActiveById(eventId, active);
        return UpdateResponse.updateResponse("Update event id : " + eventId + " to '" + active + "' successfully", null);
    }

    @GetMapping("/search")
    @Operation(summary = "Search and filter")
    public ResponseEntity<?> searchEvent(
            @RequestParam(defaultValue = "1") @Positive @NotNull Integer offset,
            @RequestParam(defaultValue = "15") @Positive @NotNull Integer limit,
            @RequestParam(required = false) String eventName,
            @RequestParam(required = false) @Positive Integer categoryId,
            @Parameter(description = "Available values : false = close, true = open")
            @RequestParam(required = false) Boolean status,
            @Parameter(description = "Format : yyyy-mm-ddThh:mm:ss. Example : 2024-06-04T12:00:00")
            @RequestParam(required = false) LocalDateTime startDateTime,
            @Parameter(description = "Format : yyyy-mm-ddThh:mm:ss. Example : 2024-06-04T12:00:00")
            @RequestParam(required = false) LocalDateTime endDateTime
    ){
        return GetAllResponse.getAllResponse("Search event successfully",
                eventService.getTotalEventRecordsFromSearch(eventName, categoryId, status, startDateTime, endDateTime),
                eventService.searchEvent(eventName, categoryId, status, startDateTime, endDateTime, offset, limit));
    }

    @PutMapping("/registration-form/{eventId}")
    @Operation(summary = "Insert, delete and update registration form")
    public ResponseEntity<?> modifyRegistrationForm(
            @PathVariable @Positive @NotNull Integer eventId,
            @RequestBody FormRequest formRequest
            ){
        return GetResponse.getResponse("Registration form is successfully modified",
                eventService.modifyRegistrationForm(eventId, formRequest));
    }
}
