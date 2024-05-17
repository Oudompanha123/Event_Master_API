package com.example.final_project.util;

import com.example.final_project.model.Member;
import org.springframework.security.core.context.SecurityContextHolder;

public class Token {
    public static Integer getOrgIdByToken(){
        Member userDetails = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getOrganization().getOrgId();
    }
}
