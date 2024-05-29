package com.example.final_project.service.Impl;

import com.example.final_project.exception.NotFoundException;
import com.example.final_project.model.Member;
import com.example.final_project.model.dto.response.member.MemberResponse;
import com.example.final_project.model.dto.response.member.NotificationResponse;
import com.example.final_project.repository.MemberRepository;
import com.example.final_project.repository.NotificationRepository;
import com.example.final_project.service.NotificationService;
import com.example.final_project.util.Token;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;

    @Override
    public List<NotificationResponse> findAllMember(Integer offset, Integer limit) {
        offset = (offset - 1) * limit;
        return notificationRepository.getAllNotifications(Token.getOrgIdByToken(), offset, limit);
    }

    @Override
    public Boolean approveMember(Integer memberId) {
        Member member = notificationRepository.getMemberByMemberId(memberId, Token.getOrgIdByToken());
        if(member == null)
            throw new NotFoundException("Member not found");

        return notificationRepository.isApprove(memberId, Token.getOrgIdByToken());
    }

    @Override
    public void rejectMemberById(Integer memberId) {
        Member member = notificationRepository.getMemberByMemberId(memberId, Token.getOrgIdByToken());
        if(member == null)
            throw new NotFoundException("Member not found");

        notificationRepository.rejectMemberById(memberId, Token.getOrgIdByToken());
    }
}
