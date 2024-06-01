package com.example.final_project.model;

import com.example.final_project.model.constant.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Member implements UserDetails {
    private Integer memberId;
    private String memberName;
    private String gender;
    private String phone;
    private String email;
    private String password;
    private String address;
    private String picture;
    private LocalDate dateOfBirth;
    private Roles role;
    private boolean isApprove;
    private Boolean isVerify = false;
    private Organization organization;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authorities = new SimpleGrantedAuthority(role.name());
        return Collections.singleton(authorities);
    }

    @Override
    public String getUsername() {
        return email;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }


    @Override
    public boolean isAccountNonLocked() {
        return true;
    }


    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    @Override
    public boolean isEnabled() {
        return true;
    }
}
