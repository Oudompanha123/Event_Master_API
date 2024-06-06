package com.example.final_project.repository;

import com.example.final_project.model.Member;
import com.example.final_project.model.dto.response.member.NotificationResponse;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserRequestRepository {

    @Select("""
        SELECT member.member_id, member_name, gender, phone, email, picture
        FROM member INNER JOIN otp on member.member_id = otp.member_id WHERE is_approve = false
                  AND member.role = 'ROLE_USER' AND otp.is_verify = true AND org_id = #{orgId} LIMIT #{limit} OFFSET #{offset};
    """)
    @Results(id = "memberMapper", value = {
            @Result(property = "memberId", column = "member_id"),
            @Result(property = "memberName", column = "member_name")
    })
    List<NotificationResponse> getAllNotifications(Integer orgId, Integer offset, Integer limit);

    @Select("""
        SELECT * FROM member WHERE member_id = #{memberId} AND org_id = #{orgId};
    """)
    @Results(id = "memberMappers", value = {
            @Result(property = "memberId", column = "member_id"),
            @Result(property = "memberName", column = "member_name"),
            @Result(property = "dateOfBirth", column = "date_of_birth"),
            @Result(property = "organization", column = "org_id", one = @One(select = "com.example.final_project.repository.MemberRepository.getOrganizationById"))
    })
    Member getMemberByMemberId(Integer memberId, Integer orgId);

    @Select("""
        UPDATE member SET is_approve = true WHERE member_id = #{memberId} AND org_id = #{orgId}
    """)
    Boolean isApprove(Integer memberId, Integer orgId);

    @Delete("""
        DELETE FROM member WHERE member_id = #{memberId} AND org_id = #{orgId}
    """)
    void rejectMemberById(Integer memberId, Integer orgId);

    @Select("""
        SELECT COUNT(*)
        FROM member INNER JOIN otp on member.member_id = otp.member_id WHERE is_approve = false
                  AND member.role = 'ROLE_USER' AND otp.is_verify = true AND org_id = #{orgId};
    """)
    Integer getAllUserRequestRecords(Integer orgId);
}
