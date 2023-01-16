package com.lookatme.server.member.service;

import com.lookatme.server.auth.utils.MemberAuthorityUtils;
import com.lookatme.server.exception.ErrorCode;
import com.lookatme.server.exception.ErrorLogicException;
import com.lookatme.server.member.entity.Follow;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.repository.FollowRepository;
import com.lookatme.server.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    private final MemberAuthorityUtils authorityUtils;

    // ** 메서드 오버로딩 **
    public Member findMember(long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ErrorLogicException(ErrorCode.MEMBER_NOT_FOUND));
    }

    // memberUniqueKey = "Email/OauthPlatform"
    public Member findMember(String memberUniqueKey) {
        String[] split = memberUniqueKey.split("/");
        return memberRepository.findByEmailAndOauthPlatform(split[0], Member.OauthPlatform.valueOf(split[1]))
                .orElseThrow(() -> new ErrorLogicException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public Member findMember(String email, Member.OauthPlatform oauthPlatform) {
        return memberRepository.findByEmailAndOauthPlatform(email, oauthPlatform)
                .orElseThrow(() -> new ErrorLogicException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public boolean hasMember(String email, Member.OauthPlatform oauthPlatform) {
        return memberRepository.existsByEmailAndOauthPlatform(email, oauthPlatform);
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
        if (!member.getNickname().equals(findMember.getNickname())) {
            verifyUniqueNickname(member.getNickname());
        }
        findMember.updateMemberProfile(member);
        return findMember;
    }

    // 로그인 되어있는 사용자가 / nickname 회원을 / followers list에 추가한다
    public void followMember(String memberUniqueKey, String nickname) {
        Member member = findMember(memberUniqueKey);
        Member opponent = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new ErrorLogicException(ErrorCode.MEMBER_NOT_FOUND));

        if (followRepository.existsByFromAndTo(member, opponent)) {
            throw new ErrorLogicException(ErrorCode.MEMBER_ALREADY_FOLLOW); // 이미 팔로우 한 회원
        }
        if (member.equals(opponent)) {
            throw new ErrorLogicException(ErrorCode.MEMBER_SELF_FOLLOW);
        }
        Follow follow = new Follow(member, opponent); // follower = 팔로우 하는 사람 / followee = 팔로우 눌린 상대
        followRepository.save(follow);
    }


    public void unfollowMember(String memberUniqueKey, String nickname) {
        Member member = findMember(memberUniqueKey);
        Member opponent = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new ErrorLogicException(ErrorCode.MEMBER_NOT_FOUND));

        if (!followRepository.existsByFromAndTo(member, opponent)) {
            throw new ErrorLogicException(ErrorCode.MEMBER_NOT_FOLLOW); // 팔로우 되어있지 않은 회원
        }

        Follow follow = followRepository.findByFromAndTo(member, opponent);
        followRepository.delete(follow);
    }

    public Page<Member> findFollowers(String memberUniqueKey, String tab, int page, int size) {
        Member member = findMember(memberUniqueKey);

        List<Member> memberList;
        switch (tab) {
            case "followee":
                memberList = member.getFollowees().stream()
                        .map(Follow::getTo).collect(Collectors.toList());
                break;
            case "follower":
                memberList = member.getFollowers().stream()
                        .map(Follow::getFrom).collect(Collectors.toList());
                break;
            default:
                throw new IllegalArgumentException();
        }
        PageRequest pageRequest = PageRequest.of(page, size);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), memberList.size());

        return new PageImpl<>(memberList.subList(start, end), pageRequest, memberList.size());
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
