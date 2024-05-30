package com.example.final_project.controller;


import com.example.final_project.model.dto.request.authentication.AdminRequest;
import com.example.final_project.model.dto.request.authentication.AuthRequest;
import com.example.final_project.model.dto.request.authentication.ForgetPasswordRequest;
import com.example.final_project.model.dto.request.authentication.UserRequest;
import com.example.final_project.model.dto.response.GetResponse;
import com.example.final_project.model.dto.response.PostResponse;
import com.example.final_project.model.dto.response.UpdateResponse;
import com.example.final_project.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final MemberService memberService;

    @PostMapping("/login")
    @Operation(summary = "login to Dashboard")
    public ResponseEntity<?> authenticate(@RequestBody @Valid AuthRequest authRequest) throws Exception {
        memberService.authenticate(authRequest.getEmail(), authRequest.getPassword());
        Object loginResponse = memberService.getToken(authRequest.getEmail());
        return PostResponse.postResponse("Login success", loginResponse);
    }

    @PostMapping("/admin-register")
    @Operation(summary = "register for admin")
    public ResponseEntity<?> adminRegister(@RequestBody @Valid AdminRequest adminRequest){
        return PostResponse.postResponse("register success as admin", memberService.adminRegister(adminRequest));
    }

    @PostMapping("/user-register")
    @Operation(summary = "register for user")
    public ResponseEntity<?> userRegister(@RequestBody @Valid UserRequest userRequest){
        return PostResponse.postResponse("register success as user", memberService.userRegister(userRequest));

    }
    @PutMapping("/forget")
    @Operation(summary = "forget password")
    public ResponseEntity<?> forgetPassword(@RequestParam @NotBlank @NotBlank @Email String email, @RequestBody @Valid ForgetPasswordRequest forgetPasswordRequest ){
        return UpdateResponse.updateResponse(memberService.forgetPassword(email,forgetPasswordRequest), null);
    }

    @PutMapping("/verify")
    @Operation(summary = "verify email by OTP code")
    public ResponseEntity<?> verifyOTP(
            @RequestParam @NotBlank @NotNull String otp,
            @RequestParam @NotBlank @NotNull String email
    ){
        return UpdateResponse.updateResponse(memberService.verifyOTP(otp, email), null);
    }

    @PostMapping("/resend")
    @Operation(summary = "resend OTP code to email")
    public ResponseEntity<?> resendOTP(@RequestParam @NotBlank @NotBlank @Email  String email){
        return PostResponse.postResponse(memberService.resendOTP(email), null);
    }

    @GetMapping("/org/{code}")
    @Operation(summary = "Get organization by organization code")
    public ResponseEntity<?> getOrganizationByCode(@PathVariable(name = "code") String orgCode){
        return GetResponse.getResponse("Get organization successfully", memberService.getOrganizationByCode(orgCode));
    }
}
