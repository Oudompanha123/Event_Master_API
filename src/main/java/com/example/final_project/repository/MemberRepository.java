package com.example.final_project.repository;

import com.example.final_project.model.Member;
import com.example.final_project.model.Organization;
import com.example.final_project.model.dto.request.authentication.AdminRequest;
import com.example.final_project.model.dto.request.authentication.ForgetPasswordRequest;
import com.example.final_project.model.dto.request.authentication.UserRequest;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface MemberRepository {
    @Select("""
           SELECT * FROM member WHERE email = #{email}
    """)
    @Results(id = "memberMapper", value = {
            @Result(property = "organization", column = "org_id", one = @One(select = "getOrganizationById")),
            @Result(property = "memberId", column = "member_id"),
            @Result(property = "memberName", column = "member_name"),
            @Result(property = "dateOfBirth", column = "date_of_birth"),
            @Result(property = "isApprove", column = "is_approve")
    })

    Member findByEmail(String email);

    @Select("""
        SELECT email from member;
    """)
    List<String> getAllEmails();

    @Select("""
        INSERT INTO member(member_name, phone, email, password, org_id, role, is_approve)
        VALUES (#{admin.adminName}, #{admin.phone}, #{admin.email}, #{admin.password}, #{orgId}, #{admin.role}, true)
        RETURNING *
    """)
    @ResultMap("memberMapper")
    Member createAdmin(@Param("admin") AdminRequest adminRequest, Integer orgId);

    @Select("""
        INSERT INTO organization VALUES (default, 'No Name', #{orgCode}, 'No Address', '')
        RETURNING org_id;
    """)
    Integer createOrganization(String orgCode);

    @Select("""
       SELECT * FROM organization WHERE org_id = #{orgId}
    """)
    @Results(id = "orgMapper", value = {
            @Result(property = "orgId", column = "org_id"),
            @Result(property = "orgName", column = "org_name"),
    })
    Organization getOrganizationById(Integer orgId);

    @Select("""
        SELECT is_verify FROM otp WHERE member_id = #{memberId} ORDER BY issued_at DESC LIMIT 1;
    """)
    boolean isVerifiedOTP(Integer memberId);


    @Select("""
        INSERT INTO otp (otp_code, issued_at, expiration, member_id) VALUES(#{otp}, #{issuedAt}, #{expired}, #{memberId})
    """)
    void createOTP(String otp, LocalDateTime issuedAt, LocalDateTime expired, Integer memberId);

    @Select("""
        SELECT org_id FROM organization WHERE code = #{orgCode};
    """)
    Integer getOrgIdByOrgCode(String orgCode);

    @Select("""
        INSERT INTO member(member_name, phone, email, password, org_id, role)
        VALUES (#{user.userName}, #{user.phone}, #{user.email}, #{user.password}, #{orgId}, #{user.role})
        RETURNING *
    """)
    @ResultMap("memberMapper")
    Member createUser(@Param("user") UserRequest userRequest, Integer orgId);

    @Select(("""
        UPDATE member SET password = #{member.password} WHERE email = #{email}
    """))
    void newPassword(String email, @Param("member") ForgetPasswordRequest forgetPasswordRequest);

    @Select("""
        SELECT otp_id FROM otp WHERE otp_code = #{otp};
    """)
    Integer isOtpExist(String otp);

    @Select("""
        SELECT issued_at FROM otp WHERE otp_code = #{otp};
    """)
    LocalDateTime issuedAt(String otp);

    @Select("""
        UPDATE otp set is_verify = true where otp_id = #{otpId};
    """)
    void updateOtpStatus(Integer otpId);

    @Select("""
        SELECT member_id FROM otp WHERE otp_id = #{otpId}
    """)
    Integer getMemberIdByOtpId(Integer otpId);

    @Select("""
        INSERT INTO category(cate_name, org_id)
        VALUES
            ('Charity', #{orgId}),
            ('Product Launches', #{orgId}),
            ('Workshop', #{orgId})
    """)
    void createDefaultEventCategory(Integer orgId);
}
