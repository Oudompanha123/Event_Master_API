package com.example.final_project.controller;

import com.example.final_project.model.dto.request.authentication.AdminRequest;
import com.example.final_project.model.dto.request.authentication.AuthRequest;
import com.example.final_project.model.dto.request.authentication.ForgetPasswordRequest;
import com.example.final_project.model.dto.request.authentication.UserRequest;
import com.example.final_project.model.dto.request.profile.ChangePasswordRequest;
import com.example.final_project.model.dto.response.GetResponse;
import com.example.final_project.model.dto.response.PostResponse;
import com.example.final_project.model.dto.response.UpdateResponse;
import com.example.final_project.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final MemberService memberService;

    @PostMapping("/login")
    @Operation(summary = "Login to Dashboard")
    public ResponseEntity<?> authenticate(@RequestBody @Valid AuthRequest authRequest) throws Exception {
        memberService.authenticate(authRequest.getEmail(), authRequest.getPassword());
        Object loginResponse = memberService.getToken(authRequest.getEmail());
        return GetResponse.getResponse("Login successfully", loginResponse);
    }

    @PostMapping("/admin-register")
    @Operation(summary = "Register for admin")
    public ResponseEntity<?> adminRegister(@RequestBody @Valid AdminRequest adminRequest){
        return PostResponse.postResponse("Register successfully as admin", memberService.adminRegister(adminRequest));
    }

    @PostMapping("/user-register")
    @Operation(summary = "Register for user")
    public ResponseEntity<?> userRegister(@RequestBody @Valid UserRequest userRequest){
        return PostResponse.postResponse("Register successfully as user", memberService.userRegister(userRequest));

    }
    @PutMapping("/set-new-password")
    @Operation(summary = "Set new password")
    public ResponseEntity<?> forgetPassword(
            @Parameter(description = "Format : example@gmail.com")
            @RequestParam @NotBlank @NotBlank @Email String email,
            @RequestBody @Valid ForgetPasswordRequest forgetPasswordRequest
    ){
        return UpdateResponse.updateResponse(memberService.forgetPassword(email,forgetPasswordRequest), null);
    }

    @PutMapping("/verify")
    @Operation(summary = "Verify email by OTP code")
    public ResponseEntity<?> verifyOTP(
            @Parameter(description = "Format : 1234")
            @RequestParam @NotBlank @NotNull String otp,
            @Parameter(description = "Format : example@gmail.com")
            @RequestParam @NotBlank @NotNull @Email String email
    ){
        return UpdateResponse.updateResponse(memberService.verifyOTP(otp, email), null);
    }

    @PostMapping("/resend")
    @Operation(summary = "Resend OTP code to email")
    public ResponseEntity<?> resendOTP(
            @Parameter(description = "Format : example@gmail.com")
            @RequestParam @NotBlank @NotBlank @Email  String email
    ){
        return PostResponse.postResponse(memberService.resendOTP(email), null);
    }

    @GetMapping("/org/{code}")
    @Operation(summary = "Get organization by organization code")
    public ResponseEntity<?> getOrganizationByCode(
            @Parameter(description = "Format : 12v9d0")
            @PathVariable(name = "code") @NotBlank @NotNull String orgCode
    ){
        return GetResponse.getResponse("Get organization successfully", memberService.getOrganizationByCode(orgCode));
    }

    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/change-password")
    @Operation(summary = "To change new password")
    public ResponseEntity<?> changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest ){
        return UpdateResponse.updateResponse(memberService.changePassword(changePasswordRequest),null);
    }
}
