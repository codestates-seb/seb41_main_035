package com.lookatme.server.member.repository;

import com.lookatme.server.member.entity.Follow;
import com.lookatme.server.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFromAndTo(Member member, Member opponent);

    Follow findByFromAndTo(Member member, Member opponent);
}
