package com.example.final_project.repository;

import com.example.final_project.model.Member;
import com.example.final_project.model.Organization;
import com.example.final_project.model.constant.Roles;
import com.example.final_project.model.dto.request.authentication.AdminRequest;
import com.example.final_project.model.dto.request.authentication.ForgetPasswordRequest;
import com.example.final_project.model.dto.request.authentication.UserRequest;
import com.example.final_project.model.dto.request.profile.ChangePasswordRequest;
import com.example.final_project.model.dto.response.member.MemberResponse;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface MemberRepository {
    @Select("""
           SELECT * FROM member WHERE email = #{email}
    """)
    @Results(id = "authMapper", value = {
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
        VALUES (#{admin.adminName}, #{admin.phone}, #{admin.email}, #{admin.password}, #{orgId}, 'ROLE_ADMIN', false)
        RETURNING *
    """)
    @ResultMap("authMapper")
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
        VALUES (#{user.userName}, #{user.phone}, #{user.email}, #{user.password}, #{orgId}, 'ROLE_USER')
        RETURNING *
    """)
    @ResultMap("authMapper")
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
        INSERT INTO category(cate_name, org_id, created_by)
        VALUES
            ('Conferences', #{orgId}, #{memberId}),
            ('Marathons And Races', #{orgId}, #{memberId})
    """)
    void createDefaultEventCategory(Integer orgId, Integer memberId);

    @Update("""
        UPDATE member SET is_approve = true WHERE member_id = #{memberId};
    """)
    void updateIsApprovedToTrue(Integer memberId);

    // member sidebar
    @Select("""
        SELECT member_id, member_name, gender, phone, email, address, picture,
        date_of_birth, role FROM member WHERE org_id = #{orgId} AND is_approve = true ORDER BY role LIMIT #{limit} OFFSET #{offset};
    """)
    @Results(id = "memberMapper", value = {
            @Result(property = "memberId", column = "member_id"),
            @Result(property = "memberName", column = "member_name"),
            @Result(property = "dateOfBirth", column = "date_of_birth")
    }
    )
    List<MemberResponse> getAllMembers(Integer offset, Integer limit, Integer orgId);

    @Select("""
        SELECT * FROM member WHERE member_id = #{memberId} AND org_id = #{orgId}
    """)
    @Results(id = "memberMappers", value = {
            @Result(property = "memberId", column = "member_id"),
            @Result(property = "memberName", column = "member_name"),
            @Result(property = "dateOfBirth", column = "date_of_birth"),
            @Result(property = "organization", column = "org_id", one = @One(select = "getOrganizationById"))
    })
    Member getMemberByMemberId(Integer memberId, Integer orgId);

    @Select("""
        DELETE FROM member WHERE member_id = #{memberId}
    """)
    void deleteMemberById(Integer memberId);

    @Select("""
        UPDATE member SET role = #{role} WHERE member_id = #{memberId} RETURNING *
    """)
    @ResultMap("memberMapper")
    MemberResponse updateMemberRole(Integer memberId, Roles role);

    @Select("""
        SELECT member_id, member_name, gender, phone, email, address, picture,
        date_of_birth, role FROM member WHERE org_id = #{orgId} AND is_approve = true
            AND member_name ILIKE CONCAT('%', #{memberName}, '%')
            ORDER BY role LIMIT #{limit} OFFSET #{offset}
        ;
    """)
    @ResultMap("memberMapper")
    List<MemberResponse> searchMemberByName(String memberName, Integer offset, Integer limit, Integer orgId);

    @Select("""
        INSERT INTO member_history VALUES
         (default, #{member.memberId}, #{member.memberName}, #{member.organization.orgId},
         #{member.role}, #{member.picture}, #{member.email}, #{member.phone})
    """)
    void insertToHistory(@Param("member") Member member);

    @Select("""
        SELECT COUNT(*) FROM member WHERE org_id = #{orgId} AND is_approve = true;
    """)
    Integer getTotalMemberRecords(Integer orgId);

    @Select("""
        SELECT COUNT(*) FROM member WHERE org_id = #{orgId} AND is_approve = true
            AND member_name ILIKE CONCAT('%', #{memberName}, '%')
    """)
    Integer getTotalMemberRecordsFromSearch(Integer orgId, String memberName);

    @Select("""
        SELECT * FROM organization WHERE code = #{orgCode};
    """)
    @Result(property = "orgId", column = "org_id")
    @Result(property = "orgName", column = "org_name")
    Organization getOrganizationByCode(String orgCode);

    @Select(("""
        UPDATE member SET password = #{member.password} WHERE email = #{email}
    """))
    void changePassword(String email, @Param("member") ChangePasswordRequest changePasswordRequest);

    @Select("""
        DELETE FROM otp WHERE member_id = #{memberId};
    """)
    void deleteOldOtp(Integer memberId);

    @Select("""
        SELECT cate_id FROM category WHERE created_by = #{memberId};
    """)
    Integer getCreatedByInCategory(Integer memberId);

    @Select("""
        SELECT otp_id FROM otp WHERE otp_code = #{otp} AND member_id = #{memberId};
    """)
    Integer memberOtp(String otp, Integer memberId);
}
