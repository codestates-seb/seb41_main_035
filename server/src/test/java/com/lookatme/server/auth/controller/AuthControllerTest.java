package com.lookatme.server.auth.controller;

import com.google.gson.Gson;
import com.lookatme.server.auth.dto.LoginRequest;
import com.lookatme.server.auth.jwt.RedisRepository;
import com.lookatme.server.exception.ErrorCode;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.entity.MemberStatus;
import com.lookatme.server.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // 테스트 간 충돌 방지
class AuthControllerTest {

    @Autowired
    private RedisRepository redisRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @DisplayName("로그인 성공 테스트")
    @Test
    void loginSuccessTest() throws Exception {
        // Given
        Member member = memberRepository.findById(1L).get();
        String tokenSubject = member.getUniqueKey();
        LoginRequest loginRequest = new LoginRequest("email_1@com", "qwe123!@#");
        String content = gson.toJson(loginRequest);

        // When
        ResultActions actions = mockMvc.perform(
                post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );
        TimeUnit.MILLISECONDS.sleep(1500);

        // Then
        // 1. OK 응답이 나와야 함
        actions.andExpect(status().isOk());

        // 2. 응답에 토큰이 둘 다 담겨있어야 함
        MvcResult mvcResult = actions.andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String accessToken = response.getHeader("Authorization");
        String refreshToken = response.getHeader("Refresh");

        assertThat(accessToken).isNotNull();
        assertThat(refreshToken).isNotNull();

        // 3. Access Token은 "Bearer " 으로 시작해야 함
        assertThat(accessToken).startsWith("Bearer ");

        // 4. RedisRepository에 Refresh Token이 저장되어 있어야 함
        boolean result = redisRepository.hasRefreshToken(refreshToken, tokenSubject);
        assertThat(result).isTrue();

        // 5. 로그인 한 member의 로그인 시도 횟수가 0으로 초기화 되야함
        assertThat(member.getLoginTryCnt()).isEqualTo(0);
    }

    @DisplayName("로그인 실패 테스트 - 1번 틀림")
    @Test
    void loginFailTest_1Time() throws Exception {
        // Given
        Member member = memberRepository.findById(1L).get();
        LoginRequest loginRequest = new LoginRequest("email_1@com", "wrongPassword123");
        String content = gson.toJson(loginRequest);

        // When
        ResultActions actions = mockMvc.perform(
                post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );


        // Then
        // 1. Bad Request 응답 + 비밀번호 틀림 응답
        actions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.LOGIN_PASSWORD_FAILED.name()))
                .andExpect(jsonPath("$.message").value(startsWith(ErrorCode.LOGIN_PASSWORD_FAILED.getValue())));

        // 2. 응답에 토큰 담겨있으면 안됨!
        MvcResult mvcResult = actions.andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String accessToken = response.getHeader("Authorization");
        String refreshToken = response.getHeader("Refresh");

        assertThat(accessToken).isNull();
        assertThat(refreshToken).isNull();

        // 3. 로그인 시도 횟수가 1 늘어야 함
        assertThat(member.getLoginTryCnt()).isEqualTo(1);
    }

    @DisplayName("로그인 실패 테스트 - 5번 틀림")
    @Test
    void loginFailTest_5Times() throws Exception {
        // Given
        Member member = memberRepository.findById(1L).get();
        LoginRequest loginRequest = new LoginRequest("email_1@com", "wrongPassword123");
        String content = gson.toJson(loginRequest);

        // When
        for(int i = 0; i < 5; i++) { // 5번 시도 전부 틀린 상태에서 요청
            mockMvc.perform(
                    post("/auth/login")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(content)
            );
        }
        ResultActions actions = mockMvc.perform(
                post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        // Then
        // 1. Bad Request 응답 + 계정 잠김 응답
        actions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.LOGIN_ACCOUNT_LOCKED.name()))
                .andExpect(jsonPath("$.message").value(ErrorCode.LOGIN_ACCOUNT_LOCKED.getValue()));

        // 2. 응답에 토큰 담겨있으면 안됨
        MvcResult mvcResult = actions.andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String accessToken = response.getHeader("Authorization");
        String refreshToken = response.getHeader("Refresh");

        assertThat(accessToken).isNull();
        assertThat(refreshToken).isNull();

        // 3. 회원 상태가 잠김 상태로 변해야 함
        assertThat(member.getLoginTryCnt()).isEqualTo(5);
        assertThat(member.getMemberStatus()).isEqualTo(MemberStatus.MEMBER_LOCKED);
    }

    @DisplayName("로그아웃 테스트")
    @Test
    void logoutTest() throws Exception {
        // Given
        // 미리 로그인 해 둠
        Member member = memberRepository.findById(1L).get();
        String tokenSubject = member.getUniqueKey();
        LoginRequest loginRequest = new LoginRequest("email_1@com", "qwe123!@#");
        String content = gson.toJson(loginRequest);
        ResultActions actions = mockMvc.perform(
                post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );
        MockHttpServletResponse response = actions.andReturn().getResponse();
        String accessToken = response.getHeader("Authorization");
        String refreshToken = response.getHeader("Refresh");

        // When
        actions = mockMvc.perform(
                post("/auth/logout")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken) // 로그인 된 회원의 액세스 토큰 전달
        );
        TimeUnit.MILLISECONDS.sleep(1500);

        // Then
        // 1. OK 응답 떠야함
        actions.andExpect(status().isOk());

        // 2. redis에 저장된 Refresh 토큰 없어야 됨 (액세스 토큰 재발급 방지)
        boolean hasRefreshToken = redisRepository.hasRefreshToken(tokenSubject, refreshToken);
        assertThat(hasRefreshToken).isFalse();

        // 3. 액세스 토큰이 블랙리스트에 올라가야함
        boolean hasAccessToken = redisRepository.hasAccessTokenInBlacklist(
                accessToken.replace("Bearer ", "")
        );
        assertThat(hasAccessToken).isTrue();
    }

    @DisplayName("토큰 재발급 테스트")
    @Test
    void tokenReissueTest() throws Exception {
        // Given
        // 미리 로그인 해 둠
        Member member = memberRepository.findById(1L).get();
        String tokenSubject = member.getUniqueKey();
        LoginRequest loginRequest = new LoginRequest("email_1@com", "qwe123!@#");
        String content = gson.toJson(loginRequest);
        ResultActions actions = mockMvc.perform(
                post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );
        MockHttpServletResponse response = actions.andReturn().getResponse();
        String accessToken = response.getHeader("Authorization");
        String refreshToken = response.getHeader("Refresh");


        // When
        actions = mockMvc.perform(
                post("/auth/reissue")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .header("Refresh", refreshToken)
        );
        TimeUnit.MILLISECONDS.sleep(1500);

        // Then
        // 1. created 응답 떠야햠
        actions.andExpect(status().isCreated());

        // 2. 응답에 새로운 Authorization 있어야 함
        response = actions.andReturn().getResponse();
        String newAccessToken = response.getHeader("Authorization");
        assertThat(newAccessToken).startsWith("Bearer ");

        // 3. 기존에 쓰던 액세스 토큰은 블랙리스트에 올라가야함
        boolean hasAccessToken = redisRepository.hasAccessTokenInBlacklist(
                accessToken.replace("Bearer ", "")
        );
        assertThat(hasAccessToken).isTrue();
    }
}