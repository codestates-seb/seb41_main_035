package com.lookatme.server.auth.dto;

import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.entity.OauthPlatform;
import lombok.Getter;

@Getter
public class LoginResponse {

    private final long memberId;
    private final String email;
    private final OauthPlatform oauthPlatform;
    private final String nickname;
    private final String profileImageUrl;

    public LoginResponse(Member member) {
        this.memberId = member.getMemberId();
        this.email = member.getEmail();
        this.oauthPlatform = member.getOauthPlatform();
        this.nickname = member.getNickname();
        this.profileImageUrl = member.getProfileImageUrl();
    }
}
