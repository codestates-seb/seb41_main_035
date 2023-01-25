package com.lookatme.server.member.service;

import com.lookatme.server.exception.ErrorCode;
import com.lookatme.server.exception.ErrorLogicException;
import com.lookatme.server.member.entity.Account;
import com.lookatme.server.member.entity.OauthPlatform;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FollowServiceTest {

    @Autowired
    private FollowService followService;


//    @DisplayName("팔로우 실패 테스트 - 셀프 팔로우 금지")
//    @Test
//    void followMemberTest_SelfFollow() {
//        // Given
//        Account member_1_Account = new Account("email_1@com", OauthPlatform.NONE);
//
//        // When
//        Throwable exception = catchThrowable(
//                () -> memberService.followMember(member_1_Account, 1L)
//        );
//
//        // Then
//        assertThat(exception).isInstanceOf(ErrorLogicException.class);
//        assertThat(exception.getMessage()).isEqualTo(ErrorCode.MEMBER_SELF_FOLLOW.getValue());
//    }
//
//    @DisplayName("팔로우 실패 테스트 - 중복 팔로우 금지")
//    @Test
//    void followMemberTest_AlreadyFollow() {
//        // Given
//        Account member_1_Account = new Account("email_1@com", OauthPlatform.NONE);
//        memberService.followMember(member_1_Account, 2L); // 1번 회원이 2번 회원을 팔로우 함
//
//        // When
//        Throwable exception = catchThrowable(
//                () -> memberService.followMember(member_1_Account, 2L) // 2번 회원을 이미 팔로우 한 상태에서 다시 팔로우 요청 보내면 예외 터져야함
//        );
//
//        // Then
//        assertThat(exception).isInstanceOf(ErrorLogicException.class);
//        assertThat(exception.getMessage()).isEqualTo(ErrorCode.MEMBER_ALREADY_FOLLOW.getValue());
//    }
//
//    @DisplayName("언팔로우 실패 테스트 - 팔로우 안한 회원")
//    @Test
//    void unfollowMemberTest_NotFollow() {
//        // Given
//        Account member_1_Account = new Account("email_1@com", OauthPlatform.NONE); // 아무도 팔로우 하지 않은 상태
//
//        // When
//        Throwable exception = catchThrowable(
//                () -> memberService.unfollowMember(member_1_Account, 2L) // 2번 회원을 이미 팔로우 한 상태에서 다시 팔로우 요청 보내면 예외 터져야함
//        );
//
//        // Then
//        assertThat(exception).isInstanceOf(ErrorLogicException.class);
//        assertThat(exception.getMessage()).isEqualTo(ErrorCode.MEMBER_NOT_FOLLOW.getValue());
//    }
//
//    @DisplayName("팔로워 찾기 실패 테스트 - 탭 없음")
//    @Test
//    void findFollowerTest_badTabRequest() {
//        // Given
//        Account member_1_Account = new Account("email_1@com", OauthPlatform.NONE);
//        String tab = "존재하지 않는 탭"; // followee / follower 탭만 처리 가능함
//
//        // When
//        Throwable exception = catchThrowable(
//                () -> memberService.findFollowers(member_1_Account, tab, 1, 10)
//        );
//
//        // Then
//        assertThat(exception).isInstanceOf(ErrorLogicException.class);
//        assertThat(exception.getMessage()).isEqualTo(ErrorCode.BAD_REQUEST.getValue());
//    }
}