package com.lookatme.server.member.service;

import com.lookatme.server.auth.utils.MemberAuthorityUtils;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberAuthorityUtils authorityUtils;

    public Member findMember(long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Member findMember(String email, Member.OauthPlatform oauthPlatform) {
        return memberRepository.findByEmailAndOauthPlatform(email, oauthPlatform)
                .orElseThrow(EntityNotFoundException::new);
    }

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member registerMember(Member member) {
        // 1. 회원/닉네임 중복검증
        verifyUniqueMember(member.getEmail(), member.getOauthPlatform());
        verifyUniqueNickname(member.getNickname());
        
        // 2. 비밀번호 암호화 및 권한 설정
        member.encodePassword(passwordEncoder);
        member.setRoles(authorityUtils);

        return memberRepository.save(member);
    }

    public Member updateMember(Member member) {
        Member findMember = findMember(member.getMemberId());
        // 닉네임 변경이 발생한 경우 닉네임 중복 검증 수행
        if(!member.getNickname().equals(findMember.getNickname())) {
            verifyUniqueNickname(member.getNickname());
        }
        findMember.updateMemberProfile(member);
        return findMember;
    }

    public void deleteMember(long memberId) {
        memberRepository.deleteById(memberId);
    }

    private void verifyUniqueMember(String email, Member.OauthPlatform oauthPlatform) {
        if (memberRepository.findByEmailAndOauthPlatform(email, oauthPlatform).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다");
        }
    }

    private void verifyUniqueNickname(String nickname) {
        if (memberRepository.findByNickname(nickname).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다");
        }
    }


}
