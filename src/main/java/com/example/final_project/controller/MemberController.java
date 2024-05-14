package com.example.final_project.controller;

import com.example.final_project.model.dto.response.GetResponse;
import com.example.final_project.service.MemberService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class MemberController {
    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<?> getAllMembers(
            @RequestParam(defaultValue = "1") @Positive Integer offset,
            @RequestParam(defaultValue = "8") @Positive Integer limit
    ){
        return GetResponse.getResponse("get all members successful", memberService.getAllMembers(offset, limit));
    }
}
