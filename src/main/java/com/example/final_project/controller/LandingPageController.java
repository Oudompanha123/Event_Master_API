package com.example.final_project.controller;

import com.example.final_project.model.dto.response.GetResponse;
import com.example.final_project.service.LandingPageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@AllArgsConstructor
@RequestMapping("/api/landing-page")
public class LandingPageController {
    private final LandingPageService landingPageService;

    @GetMapping("/getAllPopularEvent")
    @Operation(summary = "Get all popular events for each category name")
    public ResponseEntity<?> getAllPopularEvent(){
        return GetResponse.getResponse("Get all popular events successfully",
                landingPageService.getAllPopularEvent());
    }

    @GetMapping
    @Operation(summary = "Get all events by categories")
    public ResponseEntity<?> getAllEventsByCategory(){
        return GetResponse.getResponse("Get all events by categories successfully",
                landingPageService.getAllEventsByCategory());
    }

    @GetMapping("/{eventId}")
    @Operation(summary = "Get detail event by event id")
    public ResponseEntity<?> getDetailEventByEventId(@PathVariable @Positive @NotNull Integer eventId){
        return GetResponse.getResponse("Get detail event successfully",
                landingPageService.getDetailEventByEventId(eventId));
    }

    @GetMapping("/form/{eventId}")
    @Operation(summary = "Get form by event id")
    public ResponseEntity<?> getFormByEventId(@PathVariable @Positive @NotNull Integer eventId){
        return GetResponse.getResponse("Get form successfully",
                landingPageService.getFormByEventId(eventId));
    }

    @PostMapping("/search")
    @Operation(summary = "Search and filter")
    public ResponseEntity<?> searchEvent(
            @RequestParam(required = false) String eventName,
            @RequestParam(required = false) @Positive Integer categoryId,
            @Parameter(description = "Available values : false = close, true = open")
            @RequestParam(required = false) Boolean status,
            @Parameter(description = "Format : yyyy-mm-ddThh:mm:ss. Example : 2024-06-04T12:00:00")
            @RequestParam(required = false) LocalDateTime startDateTime,
            @Parameter(description = "Format : yyyy-mm-ddThh:mm:ss. Example : 2024-06-04T12:00:00")
            @RequestParam(required = false) LocalDateTime endDateTime
    ){
        return GetResponse.getResponse("Search event successfully",
                landingPageService.searchEvent(eventName, categoryId, status, startDateTime, endDateTime));
    }
}
