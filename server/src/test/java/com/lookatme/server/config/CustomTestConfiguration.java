package com.lookatme.server.config;

import com.lookatme.server.auth.handler.LoginTransactionalListener;
import com.lookatme.server.auth.jwt.JwtTokenizer;
import com.lookatme.server.auth.jwt.RedisRepository;
import com.lookatme.server.auth.userdetails.MemberDetails;
import com.lookatme.server.auth.utils.MemberAuthorityUtils;
import com.lookatme.server.exception.ErrorCode;
import com.lookatme.server.exception.ErrorLogicException;
import com.lookatme.server.member.entity.Account;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.entity.OauthPlatform;
import com.lookatme.server.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Import({
        SecurityConfiguration.class,
        MemberAuthorityUtils.class,
        JwtTokenizer.class
})
@TestConfiguration
public class CustomTestConfiguration {

    @Autowired
    private MemberAuthorityUtils authorityUtils;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private RedisRepository redisRepository;

    @MockBean
    private LoginTransactionalListener loginTransactionalListener;

    @Bean
    public UserDetailsService userDetailsService() {
        return new TestMemberDetailsService();
    }

    // 동일한 회원을 반환하도록 고정
    private class TestMemberDetailsService implements UserDetailsService {
        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            if (username.equals("none@com")) {
                throw new ErrorLogicException(ErrorCode.LOGIN_ACCOUNT_FAILED);
            }
            Member member = Member.builder()
                    .memberId(1L)
                    .account(new Account("email@com", OauthPlatform.NONE))
                    .password("{noop}qwe123!@#")
                    .nickname("nickname").build();
            return new MemberDetails(authorityUtils, member);
        }
    }
}
