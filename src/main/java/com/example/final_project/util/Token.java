package com.example.final_project.util;

import com.example.final_project.exception.UnauthorizedException;
import com.example.final_project.model.Member;
import org.springframework.security.core.context.SecurityContextHolder;

public class Token {
    public static Integer getOrgIdByToken(){
        Member userDetails = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getOrganization().getOrgId();
    }
    public static Integer getMemberIdByToken(){
        Member userDetails = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getMemberId();
    }
    public static String getEmailByToken(){
        Member userDetails = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getEmail();
    }
    public static void checkAuthorize(){
        if(Token.getOrgIdByToken() == null){
            throw new UnauthorizedException("You are not authorized to access this resource");
        }
    }
}
