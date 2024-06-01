package com.example.final_project.controller;

import com.example.final_project.model.dto.response.GetResponse;
import com.example.final_project.service.Impl.LandingPageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/landing-page")
public class LandingPageController {
    private final LandingPageService landingPageService;

    @GetMapping
    @Operation(summary = "Get all events by category")
    public ResponseEntity<?> getAllEventsByCategory(){
        return GetResponse.getResponse("Get all events by category successfully",
                landingPageService.getAllEventsByCategory());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get detail event by event id")
    public ResponseEntity<?> getDetailEventByEventId(@PathVariable(name = "id") Integer eventId){
        return GetResponse.getResponse("Get detail event successfully",
                landingPageService.getDetailEventByEventId(eventId));
    }

    @GetMapping("/form/{id}")
    @Operation(summary = "Get form by event id")
    public ResponseEntity<?> getFormByEventId(@PathVariable(name = "id") Integer eventId){
        return GetResponse.getResponse("Get form successfully",
                landingPageService.getFormByEventId(eventId));
    }
}
