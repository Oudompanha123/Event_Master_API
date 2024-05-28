package com.example.final_project.controller;

import com.example.final_project.model.dto.response.GetResponse;
import com.example.final_project.model.dto.response.UpdateResponse;
import com.example.final_project.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("api/notification")
@SecurityRequirement(name = "bearerAuth")
public class UserRequestController {
    private final NotificationService notificationService;

    // get all member is not approve
    @GetMapping
    @Operation(summary = "Get all user requests")
    public ResponseEntity<?> getAllNotifications() {
        return GetResponse.getResponse("Get all of member is not approve",notificationService.findAllMember());
    }

    // accept member
    @PutMapping("/approve/{id}")
    @Operation(summary = "accept member by id")
    public ResponseEntity<?> approveMember(@PathVariable(name = "id") Integer memberId){
        return UpdateResponse.updateResponse("Approve member id " + memberId + " successfully", notificationService.approveMember(memberId));
    }
    // reject member
    @DeleteMapping("/reject/{id}")
    @Operation(summary = "reject member by id")
    public ResponseEntity<?> rejectMember(@PathVariable(name = "id") Integer memberId){
        notificationService.rejectMemberById(memberId);
        return GetResponse.getResponse("reject member id : " + memberId + " successfully!", null );
    }
}

