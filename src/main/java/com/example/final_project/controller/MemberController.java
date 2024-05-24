package com.example.final_project.controller;

import com.example.final_project.model.constant.Roles;
import com.example.final_project.model.dto.response.GetAllResponse;
import com.example.final_project.model.dto.response.GetResponse;
import com.example.final_project.model.dto.response.UpdateResponse;
import com.example.final_project.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class MemberController {
    private final MemberService memberService;

    @GetMapping
    @Operation(summary = "Get All Members")
    public ResponseEntity<?> getAllMembers(
            @RequestParam(defaultValue = "1") @Positive Integer offset,
            @RequestParam(defaultValue = "7") @Positive Integer limit
    ){
        return GetAllResponse.getAllResponse("get all members successful", memberService.getTotalMemberRecords(), memberService.getAllMembers(offset, limit));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMemberById(@PathVariable(name = "id") Integer memberId){
        memberService.deleteMemberById(memberId);
        return GetResponse.getResponse("delete member id : " + memberId + " successfully!", null);
    }

    // update role
    @PutMapping("/{id}")
    @Operation(summary = "change role")
    public ResponseEntity<?> updateMemberRole(
            @PathVariable (name = "id") Integer memberId,
            @RequestParam Roles role
            ){
        return UpdateResponse.updateResponse("update role successfully", memberService.updateMemberRole(memberId, role));
    }

    @PostMapping("/search")
    @Operation(summary = "search by name")
    public ResponseEntity<?> searchMemberByName(
            @RequestParam String memberName,
            @RequestParam(defaultValue = "1") @Positive Integer offset,
            @RequestParam(defaultValue = "7") @Positive Integer limit)
    {
        return GetAllResponse.getAllResponse("Get member by name success", memberService.getTotalMemberRecordsFromSearch(memberName),
                memberService.searchMemberByName(memberName, offset, limit));
    }
}
