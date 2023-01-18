package com.lookatme.server.auth.userdetails;

import com.lookatme.server.auth.utils.MemberAuthorityUtils;
import com.lookatme.server.exception.ErrorCode;
import com.lookatme.server.exception.ErrorLogicException;
import com.lookatme.server.member.entity.Account;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.entity.OauthPlatform;
import com.lookatme.server.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final MemberAuthorityUtils authorityUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member findMember = memberRepository.findByAccount(new Account(username, OauthPlatform.NONE))
                .orElseThrow(() -> new ErrorLogicException(ErrorCode.MEMBER_NOT_FOUND));
        return new MemberDetails(authorityUtils, findMember);
    }

}
