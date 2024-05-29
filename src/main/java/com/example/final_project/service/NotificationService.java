package com.example.final_project.service;

import com.example.final_project.model.Member;
import com.example.final_project.model.dto.response.member.MemberResponse;
import com.example.final_project.model.dto.response.member.NotificationResponse;

import java.util.List;

public interface NotificationService {
    List<NotificationResponse> findAllMember(Integer offset, Integer limit);

    Boolean approveMember(Integer memberId);

    void rejectMemberById(Integer memberId);
}
