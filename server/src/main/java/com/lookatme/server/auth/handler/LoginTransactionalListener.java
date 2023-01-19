package com.lookatme.server.auth.handler;

import com.lookatme.server.exception.ErrorCode;
import com.lookatme.server.exception.ErrorLogicException;
import com.lookatme.server.member.entity.Account;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.entity.OauthPlatform;
import com.lookatme.server.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Component
@Slf4j
public class LoginTransactionalListener {

    private final MemberRepository memberRepository;

    public void loginSuccess(String email) {
        Member member = memberRepository.findByAccount(new Account(email, OauthPlatform.NONE)).get();
        member.loginSuccess();
    }

    public int loginFailed(String email) {
        Member savedMember = memberRepository.findByAccount(new Account(email, OauthPlatform.NONE))
                .orElseThrow(() -> new ErrorLogicException(ErrorCode.LOGIN_ACCOUNT_FAILED));
        savedMember.loginFailed();
        int loginFailedCnt = savedMember.loginFailed();
        log.error(">> 계정 {} 회 틀림", loginFailedCnt);

        return loginFailedCnt;
    }
}
