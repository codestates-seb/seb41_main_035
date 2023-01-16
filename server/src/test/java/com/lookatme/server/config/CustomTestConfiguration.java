package com.lookatme.server.config;

import com.lookatme.server.auth.jwt.JwtTokenizer;
import com.lookatme.server.auth.userdetails.MemberDetails;
import com.lookatme.server.auth.utils.MemberAuthorityUtils;
import com.lookatme.server.member.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
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

    @Bean
    public UserDetailsService userDetailsService() {
        return new TestMemberDetailsService();
    }

    // 동일한 회원을 반환하도록 고정
    private class TestMemberDetailsService implements UserDetailsService {
        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            Member member = Member.builder()
                    .memberId(1L)
                    .email("email@com")
                    .password("{noop}qwe123!@#")
                    .nickname("nickname")
                    .oauthPlatform(Member.OauthPlatform.NONE).build();
            return new MemberDetails(authorityUtils, member);
        }
    }
}
