package com.lookatme.server.config;

import com.lookatme.server.auth.dto.MemberPrincipal;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

// JWT 방식에서 딱히 쓸 일이 없어졌음.. (Authorization에 그냥 액세스 토큰 담아 보내면 됨;;)
public class WithAuthMemberSecurityContextFactory implements WithSecurityContextFactory<WithAuthMember> {

    @Override
    public SecurityContext createSecurityContext(WithAuthMember authMember) {

        SecurityContext context = SecurityContextHolder.createEmptyContext();

        MemberPrincipal principal = new MemberPrincipal(
                authMember.memberId(),
                authMember.email(),
                authMember.oauthPlatform(),
                authMember.email() + "/" + authMember.oauthPlatform().name()
        );

        Authentication auth =
                UsernamePasswordAuthenticationToken.authenticated(
                        principal, null, List.of(new SimpleGrantedAuthority(authMember.roles())));
        context.setAuthentication(auth);
        return context;
    }
}
