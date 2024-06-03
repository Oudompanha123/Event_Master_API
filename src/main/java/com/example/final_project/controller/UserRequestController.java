package com.example.final_project.controller;

import com.example.final_project.model.dto.response.GetAllResponse;
import com.example.final_project.model.dto.response.GetResponse;
import com.example.final_project.model.dto.response.UpdateResponse;
import com.example.final_project.service.UserRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user-request")
@SecurityRequirement(name = "bearerAuth")
public class UserRequestController {
    private final UserRequestService userRequestService;

    // get all member is not approve
    @GetMapping
    @Operation(summary = "Get all user requests")
    public ResponseEntity<?> getAllNotifications(
            @RequestParam(defaultValue = "1") @Positive Integer offset,
            @RequestParam(defaultValue = "8") @Positive Integer limit
    ) {
        return GetAllResponse.getAllResponse("Get all of member is not approve",
                userRequestService.getUserRequestRecords(),
                userRequestService.findAllMember(offset, limit));
    }

    // accept member
    @PutMapping("/approve/{memberId}")
    @Operation(summary = "accept member by id")
    public ResponseEntity<?> approveMember(@PathVariable Integer memberId){
        return UpdateResponse.updateResponse("Approve member id " + memberId + " successfully", userRequestService.approveMember(memberId));
    }
    // reject member
    @DeleteMapping("/reject/{memberId}")
    @Operation(summary = "reject member by id")
    public ResponseEntity<?> rejectMember(@PathVariable Integer memberId){
        userRequestService.rejectMemberById(memberId);
        return GetResponse.getResponse("reject member id : " + memberId + " successfully!", null );
    }
}

