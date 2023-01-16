package com.lookatme.server.member.repository;

import com.lookatme.server.member.entity.Account;
import com.lookatme.server.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByAccount(Account account);

    List<Member> findByAccountOrNickname(Account account, String nickname);

    Optional<Member> findByNickname(String nickname);

    boolean existsByNickname(String nickname);
}
