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
    @Operation(summary = "Get all popular events")
    public ResponseEntity<?> getAllPopularEvent(){
        return GetResponse.getResponse("Get all popular events successfully",
                landingPageService.getAllPopularEvent());
    }

    @GetMapping("/filter/{cateName}")
    @Operation(summary = "Get events by category name")
    public ResponseEntity<?> getAllEventsByCategoryName(
            @PathVariable @NotNull String cateName,
            @RequestParam(defaultValue = "1") @Positive @NotNull Integer offset,
            @RequestParam(defaultValue = "6") @Positive @NotNull Integer limit
    ){
        return GetResponse.getResponse("Get events by category name successfully",
                landingPageService.getEventByCategoryName(cateName, offset, limit));
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

    @GetMapping("/search")
    @Operation(summary = "Search and filter")
    public ResponseEntity<?> searchEvent(
            @RequestParam(required = false) String eventName,
            @RequestParam(required = false) String categoryName,
            @Parameter(description = "Available values : false = close, true = open")
            @RequestParam(required = false) Boolean status,
            @Parameter(description = "Format : yyyy-mm-ddThh:mm:ss. Example : 2024-06-04T12:00:00")
            @RequestParam(required = false) LocalDateTime startDateTime,
            @Parameter(description = "Format : yyyy-mm-ddThh:mm:ss. Example : 2024-06-04T12:00:00")
            @RequestParam(required = false) LocalDateTime endDateTime
    ){
        return GetResponse.getResponse("Search event successfully",
                landingPageService.searchEvent(eventName, categoryName, status, startDateTime, endDateTime));
    }

    @GetMapping("/getAllCategoryNames")
    @Operation(summary = "get all category names that have posted events")
    public ResponseEntity<?> getAllCategoryNames(){
        return GetResponse.getResponse("Get all category names successfully",
                landingPageService.getAllCategoryNames());
    }
}
