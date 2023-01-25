package com.lookatme.server.member.repository;

import com.lookatme.server.member.entity.Account;
import com.lookatme.server.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @EntityGraph(attributePaths = {"followees", "followers"})
    Optional<Member> findByMemberId(long memberId); // 팔로우/팔로워 수를 한방에 같이 긁어옴

    @Override
    @EntityGraph(attributePaths = {"followees", "followers"})
    Page<Member> findAll(Pageable pageable);

    Optional<Member> findByAccount(Account account);

    List<Member> findByAccountOrNickname(Account account, String nickname);

    Optional<Member> findByNickname(String nickname);

    boolean existsByNickname(String nickname);
}
