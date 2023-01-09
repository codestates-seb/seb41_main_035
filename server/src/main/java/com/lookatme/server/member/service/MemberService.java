package com.lookatme.server.member.service;

import com.lookatme.server.auth.utils.MemberAuthorityUtils;
import com.lookatme.server.exception.ErrorCode;
import com.lookatme.server.exception.ErrorLogicException;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberAuthorityUtils authorityUtils;

    public Member findMember(long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ErrorLogicException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public Member findMember(String email, Member.OauthPlatform oauthPlatform) {
        return memberRepository.findByEmailAndOauthPlatform(email, oauthPlatform)
                .orElseThrow(() -> new ErrorLogicException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public Page<Member> findMembers(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return memberRepository.findAll(pageRequest);
    }

    public Member registerMember(Member member) {
        // 1. 회원/닉네임 중복검증
        verifyUniqueMember(member.getEmail(), member.getOauthPlatform());
        verifyUniqueNickname(member.getNickname());
        
        // 2. 권한 설정
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
            throw new ErrorLogicException(ErrorCode.MEMBER_EXISTS);
        }
    }

    private void verifyUniqueNickname(String nickname) {
        if (memberRepository.findByNickname(nickname).isPresent()) {
            throw new ErrorLogicException(ErrorCode.MEMBER_NICKNAME_EXISTS);
        }
    }

    public boolean hasNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }
}
