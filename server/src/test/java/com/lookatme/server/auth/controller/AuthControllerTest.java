package com.lookatme.server.auth.controller;

import com.google.gson.Gson;
import com.lookatme.server.auth.dto.LoginDto;
import com.lookatme.server.auth.jwt.JwtTokenizer;
import com.lookatme.server.auth.service.AuthService;
import com.lookatme.server.config.CustomTestConfiguration;
import com.lookatme.server.exception.ErrorCode;
import com.lookatme.server.member.entity.Account;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.entity.OauthPlatform;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// ** 테스트 시 CustomTestConfiguration -> loadUserByUsername을 통해 고정된 데이터를 조회 **
@Import({
        CustomTestConfiguration.class,
        JwtTokenizer.class
})
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(AuthController.class)
@AutoConfigureRestDocs
class AuthControllerTest {

    @MockBean
    private AuthService authService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    private String accessToken;

    private Member savedMember;

    @BeforeEach
    void createAccessToken() {
        savedMember = Member.builder()
                .memberId(1L)
                .account(new Account("email@com", OauthPlatform.NONE))
                .nickname("nickname")
                .profileImageUrl("http://프사링크")
                .height(180)
                .weight(70)
                .build();
        accessToken = jwtTokenizer.delegateAccessToken(savedMember);
    }

    @DisplayName("로그인")
    @Test
    void loginTest() throws Exception {
        // Given
        LoginDto loginDto = new LoginDto("email@com", "qwe123!@#");
        String content = gson.toJson(loginDto);

        // When
        ResultActions actions = mockMvc.perform(
                post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        // Then
        MvcResult mvcResult = actions.andReturn();
        String accessToken = mvcResult.getResponse().getHeader("Authorization");
        assertThat(accessToken).startsWith("Bearer "); // Access Token이 Bearer으로 시작하는지 검증

        actions.andExpect(status().isOk())
                .andExpect(header().exists("Refresh")) // response 헤더에 Refresh값이 있는지 검증
                .andDo(document(
                                "auth-login",
                                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                                requestFields(
                                        List.of(
                                                fieldWithPath("email").description("로그인 이메일"),
                                                fieldWithPath("password").description("비밀번호")
                                        )
                                )
                        )
                );
    }

    @DisplayName("로그인 실패 테스트 - 비밀번호")
    @Test
    void loginFailedTest_Password() throws Exception {
        // Given
        LoginDto loginDto = new LoginDto("email@com", "wrongPassword");
        String content = gson.toJson(loginDto);

        // When
        ResultActions actions = mockMvc.perform(
                post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        // Then
        actions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.LOGIN_PASSWORD_FAILED.name()))
                .andExpect(jsonPath("$.message").value(startsWith(ErrorCode.LOGIN_PASSWORD_FAILED.getValue())));

    }

    @DisplayName("로그인 실패 테스트 - 계정")
    @Test
    void loginFailedTest_Account() throws Exception {
        // Given
        LoginDto loginDto = new LoginDto("none@com", "qwe123!@#");
        String content = gson.toJson(loginDto);

        // When
        ResultActions actions = mockMvc.perform(
                post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        // Then
        actions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.LOGIN_ACCOUNT_FAILED.name()))
                .andExpect(jsonPath("$.message").value(ErrorCode.LOGIN_ACCOUNT_FAILED.getValue()));

    }

    @DisplayName("액세스 토큰 상태 조회")
    @Test
    void checkAccessTokenAvailableTest() throws Exception {
        ResultActions actions = mockMvc.perform(
                post("/auth/jwt-test")
                        .header("Authorization", accessToken)
        );

        actions.andExpect(status().isOk())
                .andDo(document(
                        "auth-jwt-test",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint())
                ));
    }

    @DisplayName("로그아웃 테스트")
    @Test
    void logoutTest() throws Exception {
        // Given

        // When
        ResultActions actions = mockMvc.perform(
                post("/auth/logout")
                        .header("Authorization", accessToken)
        );

        // Then
        actions.andExpect(status().isOk());
    }

    // 실제 Redis에 저장 되었는지를 확인하려면 통합 테스트에서 확인해봐야함
    @DisplayName("토큰 재발급 테스트")
    @Test
    void reissueTest() throws Exception {
        // Given
        String refreshToken = jwtTokenizer.generateRefreshToken(
                savedMember.getUniqueKey(),
                jwtTokenizer.getTokenExpiration(180),
                jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey())
        );

        String newAccessToken = jwtTokenizer.delegateAccessToken(savedMember);
        given(authService.reissueAccessToken(any(), any())).willReturn(newAccessToken);

        // When
        ResultActions actions = mockMvc.perform(
                post("/auth/reissue")
                        .header("Authorization", accessToken)
                        .header("Refresh", refreshToken)
        );

        // Then
        actions.andExpect(status().isCreated());
    }
}