package com.example.final_project.controller;

import com.example.final_project.model.constant.Roles;
import com.example.final_project.model.dto.response.GetAllResponse;
import com.example.final_project.model.dto.response.GetResponse;
import com.example.final_project.model.dto.response.UpdateResponse;
import com.example.final_project.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class MemberController {
    private final MemberService memberService;

    @GetMapping
    @Operation(summary = "Get All Members")
    public ResponseEntity<?> getAllMembers(
            @RequestParam(defaultValue = "1") @Positive @NotNull Integer offset,
            @RequestParam(defaultValue = "8") @Positive @NotNull Integer limit
    ){
        return GetAllResponse.getAllResponse("Get all members successfully",
                memberService.getTotalMemberRecords(), memberService.getAllMembers(offset, limit));
    }

    @DeleteMapping("/{memberId}")
    @Operation(summary = "Delete member by id")
    public ResponseEntity<?> deleteMemberById(@PathVariable @Positive @NotNull Integer memberId){
        memberService.deleteMemberById(memberId);
        return GetResponse.getResponse("Delete member id : " + memberId + " successfully", null);
    }

    // update role
    @PutMapping("/{memberId}")
    @Operation(summary = "change role")
    public ResponseEntity<?> updateMemberRole(
            @PathVariable @Positive @NotNull Integer memberId,
            @RequestParam @Valid Roles role
            ){
        return UpdateResponse.updateResponse("Update role successfully", memberService.updateMemberRole(memberId, role));
    }

    @GetMapping("/search")
    @Operation(summary = "search by name")
    public ResponseEntity<?> searchMemberByName(
            @RequestParam @NotNull String memberName,
            @RequestParam(defaultValue = "1") @Positive @NotNull Integer offset,
            @RequestParam(defaultValue = "8") @Positive @NotNull Integer limit)
    {
        return GetAllResponse.getAllResponse("Get member by name success",
                memberService.getTotalMemberRecordsFromSearch(memberName),
                memberService.searchMemberByName(memberName, offset, limit));
    }
}
