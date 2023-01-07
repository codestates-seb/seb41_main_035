package com.lookatme.server.member.dto;

import com.lookatme.server.member.entity.Member;
import com.lookatme.server.validator.Password;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class MemberDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Post {
        @Email
        @NotBlank
        private String email;

        @Password
        private String password;

        private String nickname;

        private Member.OauthPlatform oauthPlatform; // 필수 X (없으면 NONE으로 저장)

        private int height;

        private int weight;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Patch {

        @NotBlank
        private String nickname;

        private int height;

        private int weight;

    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private String email;
        private String nickname;
        private String profileImageSrc;
        private int height;
        private int weight;
        private int followerCnt;
        private int followingCnt;
    }

}
