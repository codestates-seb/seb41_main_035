package com.lookatme.server.auth.userdetails;

import com.lookatme.server.auth.utils.MemberAuthorityUtils;
import com.lookatme.server.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@RequiredArgsConstructor
@AllArgsConstructor
public class MemberDetails implements UserDetails {

    private final MemberAuthorityUtils authorityUtils;
    @Getter private Member member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorityUtils.createAuthorities(member.getRoles());
    }

    @Override
    public String getUsername() {
        return member.getAccount().getEmail();
    }

    @Override
    public String getPassword() {
        return member.getPassword();
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