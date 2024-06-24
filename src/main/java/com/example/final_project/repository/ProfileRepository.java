package com.example.final_project.repository;

import com.example.final_project.model.Member;
import com.example.final_project.model.Organization;
import com.example.final_project.model.dto.request.profile.MemberRequest;
import com.example.final_project.model.dto.request.profile.OrganizationRequest;
import com.example.final_project.model.dto.response.profile.MemberProfileResponse;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ProfileRepository {
    @Select("""
        SELECT member_id, member_name, gender, phone, email, picture, address, date_of_birth FROM member WHERE member_id = #{memberId} AND org_id = #{orgId}
    """)
    @Results(id = "profileMapper", value = {
            @Result(property = "memberId", column = "member_id"),
            @Result(property = "memberName", column = "member_name"),
            @Result(property = "dateOfBirth", column = "date_of_birth")
    })
    MemberProfileResponse findProfile(Integer memberId, Integer orgId);

    @Select("""
        UPDATE member
        SET member_name = #{profile.memberName}, gender = #{profile.gender},
            phone = #{profile.phone}, address = #{profile.address}, picture = #{profile.picture}, date_of_birth = #{profile.dateOfBirth}
        WHERE member_id = #{profileId}
        RETURNING *;
    """)
    @ResultMap("profileMapper")
    MemberProfileResponse updateProfile(Integer profileId, @Param("profile") MemberRequest memberRequest);

    @Select("""
        SELECT * FROM organization WHERE org_id = #{orgId}
    """)
    @Results(id = "organizationMapper", value = {
            @Result(property = "orgId", column = "org_id"),
            @Result(property = "orgName", column = "org_name")
    })
    Organization findProfileOrganization(Integer orgId);

    @Select("""
        UPDATE organization SET org_name = #{org.orgName}, address = #{org.address}, logo = #{org.logo} WHERE org_id = #{orgId} RETURNING *;
    """)
    @ResultMap("organizationMapper")
    Organization updateProfileOrganization(Integer orgId, @Param("org") OrganizationRequest organizationRequest);

}
