package com.example.final_project.controller;

import com.example.final_project.model.dto.request.AttendeeRequest;
import com.example.final_project.model.dto.response.GetAllResponse;
import com.example.final_project.model.dto.response.GetResponse;
import com.example.final_project.model.dto.response.PostResponse;
import com.example.final_project.service.AttendeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/attendee")
public class AttendeeController {
    private final AttendeeService attendeeService;

    @PostMapping
    @Operation(summary = "create attendee")
    public ResponseEntity<?> createAttendee(@RequestBody AttendeeRequest attendeeRequest){
        return PostResponse.postResponse("Create attendee successfully",
                attendeeService.createAttendee(attendeeRequest));
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{eventId}")
    @Operation(summary = "Get Attendees by event id")
    public ResponseEntity<?> getAttendeesByEventId(
            @PathVariable Integer eventId,
            @RequestParam(defaultValue = "1") @Positive Integer offset,
            @RequestParam(defaultValue = "8") @Positive Integer limit
    ){
        return GetAllResponse.getAllResponse("Get all attendees by event id successfully",
                attendeeService.getTotalAttendeeRecord(eventId),
                attendeeService.getAttendeesByEventId(eventId, offset, limit)
                );
    }

    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{attendeeId}")
    @Operation(summary = "Delete an attendee by id")
    public ResponseEntity<?> deleteAttendeeById(@PathVariable Integer attendeeId){
        attendeeService.deleteAttendeeById(attendeeId);
        return GetResponse.getResponse("Delete attendee id : " + attendeeId + "  successfully", null);
    }
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/search")
    @Operation(summary = "Search attendees by name")
    public ResponseEntity<?> searchAttendeeByNameOrPhone(
            @RequestParam Integer eventId,
            @RequestParam String attendeeNameOrPhone,
            @RequestParam(defaultValue = "1") @Positive Integer offset,
            @RequestParam(defaultValue = "8") @Positive Integer limit)
    {
        return GetAllResponse.getAllResponse("Get attendee by name or phone successfully", attendeeService.getTotalAttendeeRecordsFromSearch(eventId, attendeeNameOrPhone),
                attendeeService.searchAttendeeByNameOrPhone(eventId, attendeeNameOrPhone, offset, limit));
    }
}
