package com.example.final_project.service.Impl;

import com.example.final_project.exception.BadRequestException;
import com.example.final_project.exception.NotFoundException;
import com.example.final_project.jwt.JwtService;
import com.example.final_project.model.Member;
import com.example.final_project.model.Organization;
import com.example.final_project.model.constant.Roles;
import com.example.final_project.model.dto.request.authentication.AdminRequest;
import com.example.final_project.model.dto.request.authentication.ForgetPasswordRequest;
import com.example.final_project.model.dto.request.authentication.UserRequest;
import com.example.final_project.model.dto.response.authentication.AuthResponse;
import com.example.final_project.model.dto.response.authentication.RegisterResponse;
import com.example.final_project.model.dto.response.member.MemberResponse;
import com.example.final_project.repository.MemberRepository;
import com.example.final_project.service.MemberService;
import com.example.final_project.util.OtpUtil;
import com.example.final_project.util.RandomGenerator;
import com.example.final_project.util.Token;
import com.example.final_project.util.Validation;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final EmailingServiceImpl emailingService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.findByEmail(email);
    }

    @Override
    public RegisterResponse adminRegister(AdminRequest adminRequest) throws RuntimeException {
        // check email not duplicate
        List<String> emailList = memberRepository.getAllEmails();
        if(emailList.contains(adminRequest.getEmail()))
            throw new BadRequestException("This email is already register");

        //check password and confirm password matched
        if(!adminRequest.getPassword().equals(adminRequest.getConfirmPassword())){
            throw new BadRequestException("Password and Confirm password do not matched");
        }

        // validate phone
        Validation.validatePhoneNumber(adminRequest.getPhone());

        // trim String
        adminRequest.setAdminName(adminRequest.getAdminName().trim());

        // insert data into organization table
        String orgCode = RandomGenerator.generateRandomString();
        Integer orgId = memberRepository.createOrganization(orgCode);

        // insert data into member table
        adminRequest.setPassword(bCryptPasswordEncoder.encode(adminRequest.getPassword()));
        Member member = memberRepository.createAdmin(adminRequest, orgId);

        // set 2 default values for event category (Conferences, Marathons And Races Event)
        memberRepository.createDefaultEventCategory(orgId, member.getMemberId());

        String otp = OtpUtil.generateOtp();
        // insert data into otp table
        LocalDateTime expirationDate = LocalDateTime.now().plusSeconds(60 * 2);
        LocalDateTime issuedAt = LocalDateTime.now();
        memberRepository.createOTP(otp, issuedAt, expirationDate, member.getMemberId());

        // send mail
        try {
            emailingService.sendMail(adminRequest.getEmail(), otp);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return modelMapper.map(member, RegisterResponse.class);
    }

    @Override
    public RegisterResponse userRegister(UserRequest userRequest) {
        // check email not duplicate
        List<String> emailList = memberRepository.getAllEmails();
        if(emailList.contains(userRequest.getEmail()))
            throw new BadRequestException("This email is already register");

        // validate phone
        Validation.validatePhoneNumber(userRequest.getPhone());

        //check password and confirm password matched
        if(!userRequest.getPassword().equals(userRequest.getConfirmPassword()))
            throw new BadRequestException("Password and Confirm password do not matched");

        // get org_id by org_code
        Integer orgId = memberRepository.getOrgIdByOrgCode(userRequest.getOrgCode());
        if (orgId == null){
            throw new NotFoundException("Organization code is not found");
        }

        // trim string
        userRequest.setUserName(userRequest.getUserName().trim());

        // insert data into member table
        userRequest.setPassword(bCryptPasswordEncoder.encode(userRequest.getPassword()));
        Member member = memberRepository.createUser(userRequest, orgId);

        String otp = OtpUtil.generateOtp();
        // insert data into otp table
        LocalDateTime issuedAt = LocalDateTime.now();
        LocalDateTime expirationDate = LocalDateTime.now().plusSeconds(60 * 2);
        memberRepository.createOTP(otp, issuedAt, expirationDate, member.getMemberId());

        // send mail
        try {
            emailingService.sendMail(userRequest.getEmail(), otp);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return modelMapper.map(member, RegisterResponse.class);
    }

    @Override
    public String forgetPassword(String otp, String email, ForgetPasswordRequest forgetPasswordRequest) {
        if(memberRepository.isOtpExist(otp) == null) {
            throw new NotFoundException("OTP code is not found");
        }else {
            // check email have or not
            Member member = memberRepository.findByEmail(email);
            if (member == null){
                throw new NotFoundException("Member not found");
            }

            // check member's OTP or not
            if(memberRepository.memberOtp(otp, member.getMemberId()) == null)
                throw new BadRequestException("This OTP code is not match with your email");

            // check verify otp code or not
            boolean isVerify = memberRepository.isVerifiedOTP(member.getMemberId());
            if(!isVerify)
                throw new BadRequestException("OTP code is not verified");

            // check password match or not
            if(!forgetPasswordRequest.getConfirmPassword().equals(forgetPasswordRequest.getPassword())){
                throw new BadRequestException("Password and Confirm password do not matched");
            }

            // new password to the member
            forgetPasswordRequest.setPassword(bCryptPasswordEncoder.encode(forgetPasswordRequest.getConfirmPassword()));
            memberRepository.newPassword(email,forgetPasswordRequest);

            return "Your password is changed successful";
        }
    }

    @Override
    public String verifyOTP(String otp, String email) {
        // check otp code exist in table otp
        Integer otpId = memberRepository.isOtpExist(otp);
        if(otpId == null)
            throw new NotFoundException("Wrong OTP code");
        else{
            // check otp is member opt or not
            Member member = memberRepository.findByEmail(email);
            if(member == null)
                throw new NotFoundException("Email does not exist");
            Integer memberId = memberRepository.getMemberIdByOtpId(otpId);
            if(!member.getMemberId().equals(memberId))
                throw new NotFoundException(("Wrong OTP code"));
            // check expired OTP (2 minutes long)
            if(Duration.between(memberRepository.issuedAt(otp), LocalDateTime.now()).getSeconds() < (60 * 2)){
                memberRepository.updateOtpStatus(otpId);
                if(member.getRole().equals(Roles.ROLE_ADMIN)) {
                    // change is approve to true for admin
                    memberRepository.updateIsApprovedToTrue(member.getMemberId());
                }
                return "Your account is verify successful";
            }
            else
                throw new BadRequestException("OTP code is expired");
        }
    }

    @Override
    public String resendOTP(String email) {
        Member member = memberRepository.findByEmail(email);
        if(member == null)
            throw new NotFoundException("Email does not exist");
        try {
            String otp = OtpUtil.generateOtp();
            // send mail
            emailingService.sendMail(email, otp);

            // delete member's old otp
            memberRepository.deleteOldOtp(member.getMemberId());

            // insert new record to OTP table
            LocalDateTime issuedAt = LocalDateTime.now();
            LocalDateTime expirationDate = LocalDateTime.now().plusSeconds(60 * 2);
            memberRepository.createOTP(otp, issuedAt, expirationDate, member.getMemberId());
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send OTP code.");
        }
        return "Send new OTP code Successfully";
    }

    public void authenticate(String username, String password) throws Exception {
        try {
            UserDetails member = this.loadUserByUsername(username);
            if (member == null) {
                throw new BadRequestException("Wrong Email");
            }
            if (!bCryptPasswordEncoder.matches(password, member.getPassword())) {
                throw new BadRequestException("Wrong Password");
            }
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @Override
    public Object getToken(String email) {
        // Load user details
        final UserDetails userDetails = this.loadUserByUsername(email);
        Member member = (Member) userDetails;

        // Validate based on roles and statuses
        if (!isValidRole(member.getRole())) {
            throw new NotFoundException("Role is invalid");
        }

        // for role admin, if didn't approved, means never verify otp
        if (member.getRole().equals(Roles.ROLE_ADMIN)) {
            if (!member.isApprove())
                throw new BadRequestException("This account is not verified otp code");
        // Handle non-admin roles
        }else if (member.getRole().equals(Roles.ROLE_USER) || member.getRole().equals(Roles.ROLE_SUB_ADMIN)) {
            if (!member.isApprove()) {
                if (!memberRepository.isVerifiedOTP(member.getMemberId())) {
                    throw new BadRequestException("This account is not verified yet");
                } else {
                    throw new BadRequestException("This account is not approved yet");
                }
            }
        }

        // Generate and return the token
        final String token = jwtService.generateToken(userDetails);
        return new AuthResponse(token);
    }

    // Helper method to validate roles
    private boolean isValidRole(Roles role) {
        return role.equals(Roles.ROLE_USER) || role.equals(Roles.ROLE_SUB_ADMIN) || role.equals(Roles.ROLE_ADMIN);
    }



    @Override
    public List<MemberResponse> getAllMembers(Integer offset, Integer limit) {
        offset = (offset - 1) * limit;
        Integer orgId = Token.getOrgIdByToken();
        return memberRepository.getAllMembers(offset, limit, orgId);
    }

    @Override
    public void deleteMemberById(Integer memberId) {
        // check member is existed or not
        Member member = memberRepository.getMemberByMemberId(memberId, Token.getOrgIdByToken());
        if(member == null)
            throw new NotFoundException("Member not found");
        else{
            // check member is admin or not
            if(member.getRole().equals(Roles.ROLE_ADMIN))
                throw new BadRequestException("You can't delete whose role as admin");
            if(memberRepository.getCreatedByInCategory(memberId) != null)
                throw new BadRequestException("Cannot delete this member because this member has create category");

            memberRepository.insertToHistory(member);
            memberRepository.deleteMemberById(memberId);
        }

    }

    @Override
    public MemberResponse updateMemberRole(Integer memberId, Roles role) {
        // check member is existed or not
        Member member = memberRepository.getMemberByMemberId(memberId, Token.getOrgIdByToken());
        if(member == null)
            throw new NotFoundException("Member not found");
        return memberRepository.updateMemberRole(memberId, role);
    }

    @Override
    public List<MemberResponse> searchMemberByName(String memberName, Integer offset, Integer limit) {
        memberName = memberName.trim();
        offset = (offset - 1) * limit;
        Integer orgId = Token.getOrgIdByToken();
        return memberRepository.searchMemberByName(memberName, offset, limit, orgId);
    }

    @Override
    public Integer getTotalMemberRecords(){
        Integer orgId = Token.getOrgIdByToken();
        return memberRepository.getTotalMemberRecords(orgId);
    }

    @Override
    public Integer getTotalMemberRecordsFromSearch(String memberName){
        Integer orgId = Token.getOrgIdByToken();
        return memberRepository.getTotalMemberRecordsFromSearch(orgId, memberName);
    }

    @Override
    public Organization getOrganizationByCode(String orgCode) {
        if(memberRepository.getOrganizationByCode(orgCode) == null)
            throw new NotFoundException("Organization not found");
        return memberRepository.getOrganizationByCode(orgCode);
    }
}
