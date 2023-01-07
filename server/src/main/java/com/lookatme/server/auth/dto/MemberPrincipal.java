package com.lookatme.server.auth.dto;

import com.lookatme.server.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * JWT Access Token의 claims를 이용해 컨트롤러에서 @AuthenticationPrincipal을 통해 이용할 수 있는 객체
 * 액세스 토큰에는 꼭 필요한 정보만 담아서 사용하는 것이 좋음 (개인정보 X)
 * claims는 JwtAuthenticationFilter의 delegateAccessToken 메서드에서 생성함
 */
@Getter
@ToString
@AllArgsConstructor
public class MemberPrincipal {

    private long memberId;
    private String email;
    private Member.OauthPlatform oauthPlatform;
    private List<String> roles;

    public MemberPrincipal(Map<String, Object> claims) {
        this.memberId = Long.parseLong(claims.get("memberId").toString());
        this.email = (String) claims.get("email");
        this.oauthPlatform = Member.OauthPlatform.valueOf(claims.get("oauthPlatform").toString());
        this.roles = (List<String>) claims.get("roles");
    }
}
