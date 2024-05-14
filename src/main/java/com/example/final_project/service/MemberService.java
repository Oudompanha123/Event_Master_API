package com.example.final_project.service;

import com.example.final_project.model.Member;
import com.example.final_project.model.dto.request.authentication.AdminRequest;
import com.example.final_project.model.dto.request.authentication.ForgetPasswordRequest;
import com.example.final_project.model.dto.request.authentication.UserRequest;
import com.example.final_project.model.dto.response.authentication.RegisterResponse;
import com.example.final_project.model.dto.response.member.MemberResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface MemberService extends UserDetailsService {
    RegisterResponse adminRegister(AdminRequest adminRequest) throws RuntimeException;

    RegisterResponse userRegister(UserRequest userRequest);

    String forgetPassword(String email, ForgetPasswordRequest forgetPasswordRequest);

    String verifyOTP(String otp, String email);

    String resendOTP(String email);

    List<MemberResponse> getAllMembers(Integer offset, Integer limit);
}
