package com.lookatme.server.member.service;

import com.lookatme.server.exception.ErrorCode;
import com.lookatme.server.exception.ErrorLogicException;
import com.lookatme.server.member.dto.MemberDto;
import com.lookatme.server.member.entity.Account;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.entity.MemberStatus;
import com.lookatme.server.member.entity.OauthPlatform;
import com.lookatme.server.member.repository.MemberRepository;
import com.lookatme.server.member.service.FollowService;
import com.lookatme.server.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

/**
 * 테스트 데이터는 resources > data.sql
 * 실패 테스트 위주로 작성함
 * 각 테스트가 끝날때마다 데이터가 롤백되기 때문에 db에 영향 X
 */
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private FollowService followService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager em;

    @DisplayName("회원 조회 실패 테스트 - 회원 없음")
    @Test
    void findMemberTest() {
        // Given
        long memberId = 123L; // 존재하지 않는 회원번호

        // When
        Throwable exception = catchThrowable(
                () -> memberService.findMember(memberId)
        );

        // Then
        assertThat(exception).isInstanceOf(ErrorLogicException.class);
        assertThat(exception.getMessage()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND.getValue());
    }

    @DisplayName("회원 가입 실패 테스트 - 계정 중복")
    @Test
    void registerMemberAccountTest() {
        // Given
        MemberDto.Post postDto = new MemberDto.Post(
                "email_1@com",
                "qwe123!@#",
                "닉네임",
                123, 45);

        // When
        Throwable exception = catchThrowable(
                () -> memberService.registerMember(postDto)
        );

        // Then
        assertThat(exception).isInstanceOf(ErrorLogicException.class);
        assertThat(exception.getMessage()).isEqualTo(ErrorCode.MEMBER_ACCOUNT_EXISTS.getValue());
    }

    @DisplayName("회원 가입 실패 테스트 - 닉네임 중복")
    @Test
    void registerMemberNicknameTest() {
        // Given
        MemberDto.Post postDto = new MemberDto.Post(
                "newEmail@com",
                "qwe123!@#",
                "닉네임_1",
                123, 45);

        // When
        Throwable exception = catchThrowable(
                () -> memberService.registerMember(postDto)
        );

        // Then
        assertThat(exception).isInstanceOf(ErrorLogicException.class);
        assertThat(exception.getMessage()).isEqualTo(ErrorCode.MEMBER_NICKNAME_EXISTS.getValue());
    }

    @DisplayName("회원 수정 실패 테스트 - 변경할 닉네임 중복")
    @Test
    void updateMemberTest() {
        // Given
        MemberDto.Patch patchDto = new MemberDto.Patch("닉네임_2", 123, 45);

        // When
        Throwable exception = catchThrowable(
                () -> memberService.updateMember(patchDto, 1L)
        );

        // Then
        assertThat(exception).isInstanceOf(ErrorLogicException.class);
        assertThat(exception.getMessage()).isEqualTo(ErrorCode.MEMBER_NICKNAME_EXISTS.getValue());
    }

    @DisplayName("회원 탈퇴 테스트")
    @Test
    void deleteMemberTest() {
        // Given - 1번 회원이 2번 회원 팔로우 함
        followService.follow(1L, 2L);
        em.flush();

        // When - 1번 회원 탈퇴함
        memberService.deleteMember(1L);
        em.flush();

        Throwable exception = catchThrowable(
                () -> memberService.findMember(1L)
        );

        // Then
        Member member = memberRepository.findById(1L).get();
        Member followMember = memberRepository.findByMemberId(2L).get();

        // 삭제 X
        assertThat(member.getNickname()).isEqualTo("닉네임_1");
        assertThat(member.getAccount()).isEqualTo(new Account("email_1@com", OauthPlatform.NONE));

        // 삭제 O
        assertThat(member.getMemberStatus()).isEqualTo(MemberStatus.MEMBER_WITHDRAWAL);
        assertThat(member.getPassword()).isNull();
        assertThat(member.getProfileImageUrl()).isEqualTo(Member.DEFAULT_PROFILE_IMG);

        // 팔로우 정보 초기화
        assertThat(member.getFolloweeCnt()).isEqualTo(0);
        assertThat(member.getFollowerCnt()).isEqualTo(0);
        assertThat(followMember.getFollowerCnt()).isEqualTo(0);
        assertThat(followMember.getFolloweeCnt()).isEqualTo(0);

        // 회원 조회하면 안찾아짐
        assertThat(exception).isInstanceOf(ErrorLogicException.class);
        assertThat(exception.getMessage()).isEqualTo(ErrorCode.MEMBER_NOT_FOUND.getValue());
    }
}