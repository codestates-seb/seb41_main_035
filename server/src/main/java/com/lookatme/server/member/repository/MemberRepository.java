package com.lookatme.server.member.repository;

import com.lookatme.server.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmailAndOauthPlatform(String email, Member.OauthPlatform oauthPlatform);

    Optional<Member> findByNickname(String nickname);
}
