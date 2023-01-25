package com.lookatme.server.member.service;

import com.lookatme.server.exception.ErrorCode;
import com.lookatme.server.exception.ErrorLogicException;
import com.lookatme.server.member.entity.Account;
import com.lookatme.server.member.entity.Follow;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class FollowService {

    private final FollowRepository followRepository;

    public void follow(Member member, Member opponent) {
        if (followRepository.existsByFromAndTo(member, opponent)) {
            throw new ErrorLogicException(ErrorCode.MEMBER_ALREADY_FOLLOW); // 이미 팔로우 한 회원
        }
        if (member.equals(opponent)) {
            throw new ErrorLogicException(ErrorCode.MEMBER_SELF_FOLLOW);
        }

        Follow follow = new Follow(member, opponent); // follower = 팔로우 하는 사람 / followee = 팔로우 눌린 상대
        followRepository.save(follow);
    }

    public void unFollow(Member member, Member opponent) {
        if (!followRepository.existsByFromAndTo(member, opponent)) {
            throw new ErrorLogicException(ErrorCode.MEMBER_NOT_FOLLOW); // 팔로우 되어있지 않은 회원
        }
        Follow follow = followRepository.findByFromAndTo(member, opponent);
        followRepository.delete(follow);
    }

    // 내가 팔로우 하고 있는 회원인지 확인하는 메서드
    public boolean isFollowee(long memberId, long opponentMemberId) {
        return followRepository.existsByFrom_MemberIdAndTo_MemberId(memberId, opponentMemberId);
    }

    public List<Member> findFolloweeList(Account account) {
        return followRepository.findByFrom_Account(account).stream()
                .map(Follow::getTo).collect(Collectors.toList());
    }

    public List<Member> findFollowerList(Account account) {
        return followRepository.findByTo_Account(account).stream()
                .map(Follow::getFrom).collect(Collectors.toList());
    }
}
