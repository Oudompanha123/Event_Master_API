package com.example.final_project.service.Impl;

import com.example.final_project.exception.NotFoundException;
import com.example.final_project.model.Member;
import com.example.final_project.model.dto.response.member.NotificationResponse;
import com.example.final_project.repository.UserRequestRepository;
import com.example.final_project.service.UserRequestService;
import com.example.final_project.util.Token;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserRequestServiceImpl implements UserRequestService {
    private final UserRequestRepository userRequestRepository;
    private final EmailingServiceImpl emailingService;

    @Override
    public List<NotificationResponse> findAllMember(Integer offset, Integer limit) {
        offset = (offset - 1) * limit;
        return userRequestRepository.getAllNotifications(Token.getOrgIdByToken(), offset, limit);
    }

    @Override
    public Boolean approveMember(Integer memberId) {
        Member member = userRequestRepository.getMemberByMemberId(memberId, Token.getOrgIdByToken());
        if(member == null)
            throw new NotFoundException("Member not found");
        // send mail to user
        try {
            emailingService.approveMail(member.getEmail());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return userRequestRepository.isApprove(memberId, Token.getOrgIdByToken());
    }

    @Override
    public void rejectMemberById(Integer memberId) {
        Member member = userRequestRepository.getMemberByMemberId(memberId, Token.getOrgIdByToken());
        if(member == null)
            throw new NotFoundException("Member not found");

        userRequestRepository.rejectMemberById(memberId, Token.getOrgIdByToken());
    }

    @Override
    public Integer getUserRequestRecords() {
        return userRequestRepository.getAllUserRequestRecords(Token.getOrgIdByToken());
    }
}
