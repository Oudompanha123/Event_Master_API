package com.example.final_project.controller;


import com.example.final_project.model.dto.request.authentication.AdminRequest;
import com.example.final_project.model.dto.request.authentication.AuthRequest;
import com.example.final_project.model.dto.request.authentication.ForgetPasswordRequest;
import com.example.final_project.model.dto.request.authentication.UserRequest;
import com.example.final_project.model.dto.response.PostResponse;
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

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody @Valid AuthRequest authRequest) throws Exception {
        memberService.authenticate(authRequest.getEmail(), authRequest.getPassword());
        Object loginResponse = memberService.getToken(authRequest.getEmail());
        return PostResponse.postResponse("Login success", loginResponse);
    }

    @PostMapping("/admin-register")
    public ResponseEntity<?> adminRegister(@RequestBody @Valid AdminRequest adminRequest){
        return PostResponse.postResponse("register success as admin", memberService.adminRegister(adminRequest));
    }

    @PostMapping("/user-register")
    public ResponseEntity<?> userRegister(@RequestBody @Valid UserRequest userRequest){
        return PostResponse.postResponse("register success as user", memberService.userRegister(userRequest));

    }
    @PutMapping("/forget")
    public ResponseEntity<String> forgetPassword(@RequestParam @NotBlank @NotBlank @Email String email, @RequestBody @Valid ForgetPasswordRequest forgetPasswordRequest ){
        return ResponseEntity.status(HttpStatus.OK).body(memberService.forgetPassword(email,forgetPasswordRequest));
    }

    @PutMapping("/verify")
    public ResponseEntity<String> verifyOTP(
            @RequestParam @NotBlank @NotNull String otp,
            @RequestParam @NotBlank @NotNull String email
    ){
        return ResponseEntity.status(HttpStatus.OK).body(memberService.verifyOTP(otp, email));
    }

    @PostMapping("/resend")
    public ResponseEntity<String> resendOTP(@RequestParam @NotBlank @NotBlank @Email  String email){
        return ResponseEntity.status(HttpStatus.OK).body(memberService.resendOTP(email));
    }
}
