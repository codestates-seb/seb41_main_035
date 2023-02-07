package com.lookatme.server.member.dto;

import com.lookatme.server.member.entity.Account;
import com.lookatme.server.member.entity.OauthPlatform;
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
        private String profileImageUrl;
        private OauthPlatform oauthPlatform;
        private int height;
        private int weight;

        public Post(String email, String password, String nickname, int height, int weight) {
            this.email = email;
            this.password = password;
            this.nickname = nickname;
            this.height = height;
            this.weight = weight;
        }

        public Account getAccount() {
            return new Account(email, oauthPlatform);
        }

        public String getProfileImageUrl() {
            if (profileImageUrl == null || profileImageUrl.isBlank()) {
                return "https://user-images.githubusercontent.com/74748851/212484014-b22c7726-1091-4b89-a9d5-c97d72b82068.png"; // 기본 프로필 사진
            } else {
                return this.profileImageUrl;
            }
        }
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
        private long memberId;
        private String email;
        private OauthPlatform oauthPlatform;
        private String nickname;
        private String profileImageUrl;
        private int height;
        private int weight;
        private int followerCnt;
        private int followeeCnt;
        private boolean delete; // 회원 탈퇴 유무
    }

    @Getter
    @AllArgsConstructor
    public static class ResponseWithFollow {
        private long memberId;
        private String email;
        private OauthPlatform oauthPlatform;
        private String nickname;
        private String profileImageUrl;
        private int height;
        private int weight;
        private int followerCnt;
        private int followeeCnt;
        private boolean follow;
        private boolean delete;
    }

    @Getter
    @AllArgsConstructor
    public static class SimpleResponseWithFollow {
        private long memberId;
        private String nickname;
        private String profileImageUrl;
        private boolean follow;
        private boolean delete;
    }

    @Getter
    @AllArgsConstructor
    public static class SimpleResponse {
        private long memberId;
        private String nickname;
        private String profileImageUrl;
        private boolean delete;
    }
}
