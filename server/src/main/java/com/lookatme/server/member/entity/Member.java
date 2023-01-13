package com.lookatme.server.member.entity;

import com.lookatme.server.audit.BaseTimeEntity;
import com.lookatme.server.auth.utils.MemberAuthorityUtils;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long memberId;

    @Column(updatable = false)
    private String email;

    private String password;

    private String nickname;

    private String profileImageUrl;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = List.of("USER");

    private int height;

    private int weight;

    @OneToMany(mappedBy = "from")
    List<Follow> followees = new ArrayList<>(); // 내가 팔로우 한 사람

    @OneToMany(mappedBy = "to")
    List<Follow> followers = new ArrayList<>(); // 나를 팔로우하는 사람

    @Enumerated(EnumType.STRING)
    public MemberStatus memberStatus;

    @Column(updatable = false)
    @Enumerated(EnumType.STRING)
    public OauthPlatform oauthPlatform; // email + oauthPlatform 세트가 유니크 해야함 (email 자체는 겹칠 수 있음)

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    public enum MemberStatus {
        MEMBER_ACTIVE("활동중"),
        MEMBER_SLEEP("휴면 상태"),
        MEMBER_QUIT("탈퇴 상태");

        @Getter
        private String status;

        MemberStatus(String status) {
            this.status = status;
        }
    }

    public enum OauthPlatform {
        NONE, GOOGLE
    }


    @Builder
    public Member(long memberId, String email, String password, String nickname, int height, int weight, String profileImageUrl, OauthPlatform oauthPlatform) {
        this.memberId = memberId;
        this.email = email; // 필수
        this.password = password; // 필수
        this.nickname = nickname; // 선택값 (없으면 이메일을 기본으로 사용)
        this.height = height; // 선택값
        this.weight = weight; // 선택값
        this.memberStatus = MemberStatus.MEMBER_ACTIVE;
        this.profileImageUrl = profileImageUrl;
        this.oauthPlatform = oauthPlatform == null ? OauthPlatform.NONE : oauthPlatform; // null이면 NONE으로 저장
    }
    
    public String getUniqueKey() { // 사용자를 고유하게 식별할 수 있는 문자열
        return String.format("%s/%s", email, oauthPlatform.name());
    }

    public void updateMemberProfile(Member member) {
        this.nickname = member.getNickname();
        if(member.getProfileImageUrl() != null) {
            this.profileImageUrl = member.getProfileImageUrl();
        }
        this.height = member.getHeight();
        this.weight = member.getWeight();
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    public void setRoles(MemberAuthorityUtils authorityUtils) {
        this.roles = authorityUtils.createRoles(email);
    }

    public int getFollowerCnt() {
        return followers.size();
    }

    public int getFolloweeCnt() {
        return followees.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return memberId == member.memberId && email.equals(member.email) && oauthPlatform == member.oauthPlatform;
    }

    @Override
    public int hashCode() {
        return Objects.hash(memberId, email, oauthPlatform);
    }
}
