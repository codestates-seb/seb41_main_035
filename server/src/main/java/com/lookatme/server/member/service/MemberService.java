package com.lookatme.server.member.service;

import com.lookatme.server.auth.service.AuthService;
import com.lookatme.server.auth.utils.MemberAuthorityUtils;
import com.lookatme.server.exception.ErrorCode;
import com.lookatme.server.exception.ErrorLogicException;
import com.lookatme.server.member.dto.MemberDto;
import com.lookatme.server.member.entity.Account;
import com.lookatme.server.member.entity.Follow;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.entity.OauthPlatform;
import com.lookatme.server.member.mapper.MemberMapper;
import com.lookatme.server.member.repository.FollowRepository;
import com.lookatme.server.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final AuthService authService;
    private final MemberMapper mapper;
    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    private final MemberAuthorityUtils authorityUtils;
    private final PasswordEncoder passwordEncoder;

    // ** 메서드 오버로딩 **
    public MemberDto.Response findMember(long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ErrorLogicException(ErrorCode.MEMBER_NOT_FOUND));
        return mapper.memberToMemberResponse(member);
    }

    public Page<MemberDto.Response> findMembers(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Member> memberPage = memberRepository.findAll(pageRequest);
        List<MemberDto.Response> responseList = mapper.memberListToMemberResponseList(memberPage.getContent());
        return new PageImpl<>(responseList, pageRequest, responseList.size());
    }

    public MemberDto.Response registerMember(MemberDto.Post postDto) {
        // 1. 회원 중복 검증
        Account account = postDto.getAccount();
        verifyUniqueMember(account, postDto.getNickname());

        // 2. Dto -> Entity 변환, 권한 설정
        Member member = mapper.memberPostDtoToMember(postDto);
        String encodedPassword = passwordEncoder.encode(postDto.getPassword());
        member.setPassword(encodedPassword);
        member.setRoles(authorityUtils);

        // 3. 저장 후 Entity -> Response Dto 변환
        Member savedMember = memberRepository.save(member);
        return mapper.memberToMemberResponse(savedMember);
    }

    public MemberDto.Response updateMember(MemberDto.Patch patchDto, long memberId) {
        Member findMember = getMember(memberId);
        // 닉네임 변경이 발생한 경우 닉네임 중복 검증 수행
        if (!patchDto.getNickname().equals(findMember.getNickname())) {
            verifyUniqueNickname(patchDto.getNickname());
        }
        findMember.updateMemberProfile(patchDto.getNickname(), patchDto.getProfileImageUrl(), patchDto.getHeight(), patchDto.getWeight());
        return mapper.memberToMemberResponse(findMember);
    }

    // 로그인 되어있는 사용자가 / nickname 회원을 / followers list에 추가한다
    public void followMember(String memberUniqueKey, String nickname) {
        Member member = getMember(memberUniqueKey);
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
        Member member = getMember(memberUniqueKey);
        Member opponent = memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new ErrorLogicException(ErrorCode.MEMBER_NOT_FOUND));

        if (!followRepository.existsByFromAndTo(member, opponent)) {
            throw new ErrorLogicException(ErrorCode.MEMBER_NOT_FOLLOW); // 팔로우 되어있지 않은 회원
        }

        Follow follow = followRepository.findByFromAndTo(member, opponent);
        followRepository.delete(follow);
    }

    public Page<MemberDto.Response> findFollowers(String memberUniqueKey, String tab, int page, int size) {
        Member member = getMember(memberUniqueKey);

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
                throw new ErrorLogicException(ErrorCode.BAD_REQUEST);
        }

        List<MemberDto.Response> memberResponseList = mapper.memberListToMemberResponseList(memberList);

        PageRequest pageRequest = PageRequest.of(page, size);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), memberResponseList.size());

        return new PageImpl<>(memberResponseList.subList(start, end), pageRequest, memberResponseList.size());
    }

    public void deleteMember(long memberId) {
        memberRepository.deleteById(memberId);
    }

    private void verifyUniqueMember(Account account, String nickname) {
        // 계정 정보(email+oauth) 또는 닉네임 목록 조회 -> 총 4가지 케이스 존재. 쿼리는 1개로 해결
        // 아무것도 안겹치는 경우 (성공) / 계정 정보만 겹치는 경우, 닉네임만 겹치는 경우, 둘 다 겹치는 경우 (실패)
        memberRepository.findByAccountOrNickname(account, nickname)
                .forEach(member -> {
                    if (member.getNickname().equals(nickname)) {
                        throw new ErrorLogicException(ErrorCode.MEMBER_NICKNAME_EXISTS);
                    } else {
                        throw new ErrorLogicException(ErrorCode.MEMBER_ACCOUNT_EXISTS);
                    }
                });
    }

    private void verifyUniqueNickname(String nickname) {
        if (memberRepository.findByNickname(nickname).isPresent()) {
            throw new ErrorLogicException(ErrorCode.MEMBER_NICKNAME_EXISTS);
        }
    }

    private Member getMember(long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ErrorLogicException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private Member getMember(String memberUniqueKey) {
        Account account = Member.uniqueKeyToAccount(memberUniqueKey);
        return memberRepository.findByAccount(account)
                .orElseThrow(() -> new ErrorLogicException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
