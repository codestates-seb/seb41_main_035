package com.lookatme.server.auth.controller;

import com.google.gson.Gson;
import com.lookatme.server.auth.dto.LoginDto;
import com.lookatme.server.auth.jwt.JwtTokenizer;
import com.lookatme.server.auth.jwt.RedisRepository;
import com.lookatme.server.auth.service.AuthService;
import com.lookatme.server.config.CustomTestConfiguration;
import com.lookatme.server.member.dto.MemberDto;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.mapper.MemberMapperImpl;
import com.lookatme.server.member.service.MemberService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import({
        CustomTestConfiguration.class,
        JwtTokenizer.class,
        MemberMapperImpl.class
})
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(AuthController.class)
@AutoConfigureRestDocs
class AuthControllerTest {

    @MockBean
    private MemberService memberService;

    @MockBean
    private AuthService authService;

    @MockBean
    private RedisRepository redisRepository;

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
                .email("email@com")
                .nickname("nickname")
                .profileImageUrl("http://프사링크")
                .oauthPlatform(Member.OauthPlatform.NONE)
                .height(180)
                .weight(70)
                .build();
        accessToken = jwtTokenizer.delegateAccessToken(savedMember);
    }

    @DisplayName("회원 가입")
    @Test
    void registerMemberTest() throws Exception {
        // Given
        MemberDto.Post postDto = new MemberDto.Post(
                "email@com",
                "{noop}pwd123!@#",
                "닉네임",
                Member.OauthPlatform.NONE,
                180, 70);
        String content = gson.toJson(postDto);
        given(memberService.registerMember(any())).willReturn(savedMember);

        // When
        ResultActions actions = mockMvc.perform(
                post("/auth/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        // Then
        actions.andExpect(status().isCreated());
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