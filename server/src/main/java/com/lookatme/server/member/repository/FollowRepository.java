package com.lookatme.server.member.repository;

import com.lookatme.server.member.entity.Account;
import com.lookatme.server.member.entity.Follow;
import com.lookatme.server.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByFromAndTo(Member member, Member opponent);

    boolean existsByFrom_MemberIdAndTo_MemberId(long memberId, long opponentMemberId);

    Follow findByFromAndTo(Member member, Member opponent);

    Follow findByFrom_MemberIdAndTo_MemberId(long memberId, long opponentId);

    List<Follow> findByFrom_Account(Account account);

    List<Follow> findByTo_Account(Account account);

    List<Follow> findByFrom_MemberId(long memberId);

    @Query("select f from Follow f where f.to.memberId=:memberId or f.from.memberId=:memberId")
    List<Follow> findFollowListByMemberId(@Param("memberId") long memberId);
}
