package com.example.final_project.controller;

import com.example.final_project.model.dto.request.AttendeeRequest;
import com.example.final_project.model.dto.response.GetAllResponse;
import com.example.final_project.model.dto.response.GetResponse;
import com.example.final_project.model.dto.response.PostResponse;
import com.example.final_project.service.AttendeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/attendees")
public class AttendeeController {
    private final AttendeeService attendeeService;

    @PostMapping("/create")
    @Operation(summary = "Create attendee")
    public ResponseEntity<?> createAttendee(@RequestBody @Valid AttendeeRequest attendeeRequest){
        return PostResponse.postResponse("Create attendee successfully",
                attendeeService.createAttendee(attendeeRequest));
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{eventId}")
    @Operation(summary = "Get Attendees by event id")
    public ResponseEntity<?> getAttendeesByEventId(
            @PathVariable @Positive @NotNull Integer eventId,
            @RequestParam(defaultValue = "1") @Positive @NotNull Integer offset,
            @RequestParam(defaultValue = "8") @Positive @NotNull Integer limit
    ){
        return GetAllResponse.getAllResponse("Get all attendees by event id successfully",
                attendeeService.getTotalAttendeeRecord(eventId),
                attendeeService.getAttendeesByEventId(eventId, offset, limit)
                );
    }

    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{attendeeId}")
    @Operation(summary = "Delete an attendee by id")
    public ResponseEntity<?> deleteAttendeeById(@PathVariable @Positive @NotNull Integer attendeeId){
        attendeeService.deleteAttendeeById(attendeeId);
        return GetResponse.getResponse("Delete attendee id : " + attendeeId + "  successfully", null);
    }
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/search")
    @Operation(summary = "Search attendees by name")
    public ResponseEntity<?> searchAttendeeByNameOrPhone(
            @RequestParam @Positive @NotNull Integer eventId,
            @Parameter(description = "Input attendee name or phone number. Format : dara or 0123456789 ")
            @RequestParam(required = false) String attendeeNameOrPhone,
            @RequestParam(defaultValue = "1") @Positive @NotNull Integer offset,
            @RequestParam(defaultValue = "8") @Positive @NotNull Integer limit)
    {
        return GetAllResponse.getAllResponse("Get attendee by name or phone successfully", attendeeService.getTotalAttendeeRecordsFromSearch(eventId, attendeeNameOrPhone),
                attendeeService.searchAttendeeByNameOrPhone(eventId, attendeeNameOrPhone, offset, limit));
    }
}
