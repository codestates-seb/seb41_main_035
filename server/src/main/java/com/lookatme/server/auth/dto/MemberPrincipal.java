package com.lookatme.server.auth.dto;

import com.lookatme.server.member.entity.Account;
import com.lookatme.server.member.entity.OauthPlatform;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

/**
 * JWT Access Token의 claims를 이용해 컨트롤러에서 @AuthenticationPrincipal을 통해 이용할 수 있는 객체
 * claims는 JwtTokenizer->generateClaims 메서드에서 생성함
 */
@Getter
@ToString
@AllArgsConstructor
public class MemberPrincipal {

    private long memberId;
    private Account account;
    private List<String> roles;

    public MemberPrincipal(Map<String, Object> claims) {
        this.memberId = Long.parseLong(claims.get("memberId").toString());
        Map<String, String> map = (Map<String, String>) claims.get("account");
        this.account = new Account(map.get("email"), OauthPlatform.valueOf(map.get("oauthPlatform")));
        this.roles = (List<String>) claims.get("roles");
    }

    public String getMemberUniqueKey() {
        return account.toString();
    }

    public String getEmail() {
        return account.getEmail();
    }

    public OauthPlatform getOauthPlatform() {
        return account.getOauthPlatform();
    }
}
