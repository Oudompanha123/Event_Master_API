package com.example.final_project.service;

import com.example.final_project.model.dto.response.member.NotificationResponse;

import java.util.List;

public interface UserRequestService {
    List<NotificationResponse> findAllMember(Integer offset, Integer limit);

    Boolean approveMember(Integer memberId);

    void rejectMemberById(Integer memberId);

    Integer getUserRequestRecords();
}
