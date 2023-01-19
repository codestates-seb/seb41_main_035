package com.lookatme.server.config;

import com.lookatme.server.member.entity.Member;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithAuthMemberSecurityContextFactory.class)
public @interface WithAuthMember {
    long memberId() default 1L;
    String email() default "email@com";
    Member.OauthPlatform oauthPlatform() default Member.OauthPlatform.NONE;
    String roles() default "ROLE_USER";
}
