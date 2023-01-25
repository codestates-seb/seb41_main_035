package com.lookatme.server.member.service;

import com.lookatme.server.exception.ErrorCode;
import com.lookatme.server.exception.ErrorLogicException;
import com.lookatme.server.member.entity.Account;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.entity.OauthPlatform;
import com.lookatme.server.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FollowServiceTest {

    @Autowired
    private FollowService followService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("팔로우 실패 테스트 - 셀프 팔로우 금지")
    @Test
    void followMemberTest_SelfFollow() {
        // When
        Throwable exception = catchThrowable(
                () -> followService.follow(1L, 1L)
        );

        // Then
        assertThat(exception).isInstanceOf(ErrorLogicException.class);
        assertThat(exception.getMessage()).isEqualTo(ErrorCode.MEMBER_SELF_FOLLOW.getValue());
    }

    @DisplayName("팔로우 실패 테스트 - 중복 팔로우 금지")
    @Test
    void followMemberTest_AlreadyFollow() {
        // Given
        followService.follow(1L, 2L); // 1번 회원이 2번 회원을 팔로우 함

        // When
        Throwable exception = catchThrowable(
                () -> followService.follow(1L, 2L) // 2번 회원을 이미 팔로우 한 상태에서 다시 팔로우 요청 보내면 예외 터져야함
        );

        // Then
        assertThat(exception).isInstanceOf(ErrorLogicException.class);
        assertThat(exception.getMessage()).isEqualTo(ErrorCode.MEMBER_ALREADY_FOLLOW.getValue());
    }

    @DisplayName("언팔로우 실패 테스트 - 팔로우 안한 회원")
    @Test
    void unfollowMemberTest_NotFollow() {
        // Given
        // 아무도 팔로우 하지 않은 상태

        // When
        Throwable exception = catchThrowable(
                () -> followService.unFollow(1L, 2L)
        );

        // Then
        assertThat(exception).isInstanceOf(ErrorLogicException.class);
        assertThat(exception.getMessage()).isEqualTo(ErrorCode.MEMBER_NOT_FOLLOW.getValue());
    }

    @DisplayName("팔로워 찾기 실패 테스트 - 탭 없음")
    @Test
    void findFollowerTest_badTabRequest() {
        // Given
        Account member_1_Account = new Account("email_1@com", OauthPlatform.NONE);
        String tab = "존재하지 않는 탭"; // followee / follower 탭만 처리 가능함

        // When
        Throwable exception = catchThrowable(
                () -> followService.findFollows(member_1_Account, tab, 1, 10)
        );

        // Then
        assertThat(exception).isInstanceOf(ErrorLogicException.class);
        assertThat(exception.getMessage()).isEqualTo(ErrorCode.BAD_REQUEST.getValue());
    }

    @DisplayName("팔로우 테스트")
    @Test
    void followMemberTest() {
        // When
        followService.follow(1L, 2L); // 1번 회원이 2번 회원을 팔로우 하면

        // Then
        Member follower = memberRepository.findById(1L).get();
        Member followee = memberRepository.findById(2L).get();

        assertThat(follower.getFolloweeCnt()).isEqualTo(1); // 1번 회원의 followee 수 + 1
        assertThat(follower.getFollowerCnt()).isEqualTo(0);
        assertThat(followee.getFollowerCnt()).isEqualTo(1); // 2번 회원의 follower 수 + 1
        assertThat(followee.getFolloweeCnt()).isEqualTo(0);
    }

    @DisplayName("언팔로우 테스트")
    @Test
    void unfollowMemberTest() {
        // Given - 1번 회원이 2번, 3번 회원 두명을 팔로우 함
        followService.follow(1L, 2L);
        followService.follow(1L, 3L);

        // When - 1번 회원이 3번 회원을 언팔로우 함
        followService.unFollow(1L, 3L);
        em.flush(); // 강제 반영 (트랜잭션이 안끝난 상태임)

        // Then
        Member follower = memberRepository.findById(1L).get();
        Member followee = memberRepository.findById(2L).get();
        Member unfollowee = memberRepository.findById(3L).get();

        assertThat(follower.getFolloweeCnt()).isEqualTo(1); // 1번 회원의 followee 수 = 1 (2번)
        assertThat(follower.getFollowerCnt()).isEqualTo(0);
        assertThat(unfollowee.getFollowerCnt()).isEqualTo(0); // 3번 회원의 follower 수 = 0
        assertThat(unfollowee.getFolloweeCnt()).isEqualTo(0);
        assertThat(followee.getFollowerCnt()).isEqualTo(1); // 2번 회원의 follower 수 = 1 (1번)
        assertThat(followee.getFolloweeCnt()).isEqualTo(0);
    }
}