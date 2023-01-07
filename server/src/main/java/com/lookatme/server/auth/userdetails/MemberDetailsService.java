package com.lookatme.server.auth.userdetails;

import com.lookatme.server.auth.utils.MemberAuthorityUtils;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;

@RequiredArgsConstructor
@Component
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final MemberAuthorityUtils authorityUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member findMember = memberRepository.findByEmailAndOauthPlatform(username, Member.OauthPlatform.NONE)
                .orElseThrow(() -> new EntityNotFoundException("회원이 존재하지 않습니다"));
        return new MemberDetails(authorityUtils, findMember);
    }

}
