package com.example.final_project.controller;

import com.example.final_project.model.dto.response.GetAllResponse;
import com.example.final_project.model.dto.response.GetResponse;
import com.example.final_project.model.dto.response.UpdateResponse;
import com.example.final_project.service.UserRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user-requests")
@SecurityRequirement(name = "bearerAuth")
public class UserRequestController {
    private final UserRequestService userRequestService;

    @GetMapping
    @Operation(summary = "Get all user requests")
    public ResponseEntity<?> getAllUserRequest(
            @RequestParam(defaultValue = "1") @Positive @NotNull Integer offset,
            @RequestParam(defaultValue = "8") @Positive @NotNull Integer limit
    ) {
        return GetAllResponse.getAllResponse("Get all of member is not approve",
                userRequestService.getUserRequestRecords(),
                userRequestService.findAllMember(offset, limit));
    }

    @PutMapping("/approve/{memberId}")
    @Operation(summary = "Accept member by id")
    public ResponseEntity<?> approveMember (@PathVariable @Positive @NotNull Integer memberId)
    {

        return UpdateResponse.updateResponse("Approve member id " + memberId + " successfully",
                userRequestService.approveMember(memberId));

    }

    @DeleteMapping("/reject/{memberId}")
    @Operation(summary = "Reject member by id")
    public ResponseEntity<?> rejectMember(@PathVariable @Positive @NotNull Integer memberId){
        userRequestService.rejectMemberById(memberId);
        return GetResponse.getResponse("Reject member id : " + memberId + " successfully", null );
    }
}

