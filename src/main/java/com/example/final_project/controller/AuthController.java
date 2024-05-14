package com.example.final_project.controller;

import com.example.final_project.exception.BadRequestException;
import com.example.final_project.exception.NotFoundException;
import com.example.final_project.jwt.JwtService;
import com.example.final_project.model.Member;
import com.example.final_project.model.dto.request.authentication.AdminRequest;
import com.example.final_project.model.dto.request.authentication.AuthRequest;
import com.example.final_project.model.dto.request.authentication.ForgetPasswordRequest;
import com.example.final_project.model.dto.request.authentication.UserRequest;
import com.example.final_project.model.dto.response.authentication.*;
import com.example.final_project.model.dto.response.authentication.PostResponse;
import com.example.final_project.repository.MemberRepository;
import com.example.final_project.service.MemberService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final MemberService memberService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    private void authenticate(String username, String password) throws Exception {
        try {
            UserDetails member = memberService.loadUserByUsername(username);
            if (member == null) {
                throw new BadRequestException("Wrong Email");
            }
            if (!passwordEncoder.matches(password, member.getPassword())) {
                throw new BadRequestException("Wrong Password");
            }
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody @Valid AuthRequest authRequest) throws Exception {
        authenticate(authRequest.getEmail(), authRequest.getPassword());
        final UserDetails userDetails = memberService.loadUserByUsername(authRequest.getEmail());
        final String token = jwtService.generateToken(userDetails);

        // check user is verified or not
        Member member = (Member) userDetails;
        boolean isVerifiedOTP = memberRepository.isVerifiedOTP(member.getMemberId());
        if(!isVerifiedOTP)
            throw new NotFoundException("This account is not verify yet !");

        // if role is user, check is approved or not
        if(member.getRole().equals("user") && !member.isApprove())
            throw new NotFoundException("This account is not approve yet");

        // check role, if user, return only token but if admin, return token and org profile
        if(member.getRole().equals("user"))
            return ResponseEntity.status(HttpStatus.OK).body(new AuthResponse(token));
        else if(member.getRole().equals("admin"))
            return ResponseEntity.status(HttpStatus.OK).body(new AdminLoginResponse(token, member.getOrganization()));
        else
            throw new BadRequestException("role is invalid!");

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
