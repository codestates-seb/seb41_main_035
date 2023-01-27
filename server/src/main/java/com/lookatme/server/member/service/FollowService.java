package com.lookatme.server.member.service;

import com.lookatme.server.exception.ErrorCode;
import com.lookatme.server.exception.ErrorLogicException;
import com.lookatme.server.member.dto.MemberDto;
import com.lookatme.server.member.entity.Account;
import com.lookatme.server.member.entity.Follow;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.mapper.MemberMapper;
import com.lookatme.server.member.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberMapper mapper;

    public void follow(long memberId, long opponentId) {
        if (followRepository.existsByFrom_MemberIdAndTo_MemberId(memberId, opponentId)) {
            throw new ErrorLogicException(ErrorCode.MEMBER_ALREADY_FOLLOW); // 이미 팔로우 한 회원
        }
        if (memberId == opponentId) {
            throw new ErrorLogicException(ErrorCode.MEMBER_SELF_FOLLOW);
        }
        Member member = getEmptyMember(memberId);
        Member opponent = getEmptyMember(opponentId);

        Follow follow = new Follow(member, opponent); // follower = 팔로우 하는 사람 / followee = 팔로우 눌린 상대
        followRepository.save(follow);
    }

    public void unFollow(long memberId, long opponentId) {
        if (!followRepository.existsByFrom_MemberIdAndTo_MemberId(memberId, opponentId)) {
            throw new ErrorLogicException(ErrorCode.MEMBER_NOT_FOLLOW); // 팔로우 되어있지 않은 회원
        }
        Follow follow = followRepository.findByFrom_MemberIdAndTo_MemberId(memberId, opponentId);
        followRepository.delete(follow);
    }

    // 내가 팔로우 하고 있는 회원인지 확인하는 메서드
    public boolean isFollowee(long memberId, long opponentMemberId) {
        return followRepository.existsByFrom_MemberIdAndTo_MemberId(memberId, opponentMemberId);
    }

    @Transactional(readOnly = true)
    public Page<MemberDto.Response> findFollows(Account account, String tab, int page, int size) {
        List<Member> memberList;
        switch (tab) {
            case "followee":
                memberList = followRepository.findByFrom_Account(account).stream()
                        .map(Follow::getTo).collect(Collectors.toList());
                break;
            case "follower":
                memberList = followRepository.findByTo_Account(account).stream()
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

    /**
     * memberId 회원이 현재 팔로우 중인 회원 id set 반환 (중복 X)
     */
    public Set<Long> getFollowMemberIdSet(long memberId) {
        return followRepository.findByFrom_MemberId(memberId).stream()
                .map(follow -> follow.getTo().getMemberId())
                .collect(Collectors.toSet());
    }

    private Member getEmptyMember(long memberId) {
        return Member.builder()
                .memberId(memberId).build();
    }

    /**
     * 회원 탈퇴 시 해당 회원ID가 들어간 팔로우/팔로워 목록 전부 제거
     */
    public void withdrawalMember(long memberId) {
        followRepository.deleteAll(followRepository.findFollowListByMemberId(memberId));
    }
}
