package com.lookatme.server.member.controller;

import com.google.gson.Gson;
import com.lookatme.server.auth.controller.AuthController;
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
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({
        CustomTestConfiguration.class,
        MemberMapperImpl.class,
        JwtTokenizer.class
})
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest({MemberController.class, AuthController.class})
@AutoConfigureRestDocs
class MemberControllerTest {

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
                .height(180)
                .weight(70)
                .build();
        accessToken = jwtTokenizer.delegateAccessToken(savedMember);
    }

    @DisplayName("회원 조회")
    @Test
    void getMemberTest() throws Exception {
        // Given
        given(memberService.findMember(savedMember.getMemberId())).willReturn(savedMember);

        // When
        ResultActions actions = mockMvc.perform(
                get("/members/{memberId}", savedMember.getMemberId())
                        .accept(MediaType.APPLICATION_JSON)
        );

        // Then
        actions.andExpect(status().isOk())
                .andDo(document(
                        "get-member",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("memberId").description("회원 번호")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("email").type(STRING).description("이메일"),
                                        fieldWithPath("nickname").type(STRING).description("닉네임"),
                                        fieldWithPath("profileImageUrl").type(STRING).description("프로필 사진 주소"),
                                        fieldWithPath("height").type(NUMBER).description("키"),
                                        fieldWithPath("weight").type(NUMBER).description("몸무게"),
                                        fieldWithPath("followerCnt").type(NUMBER).description("팔로워 수"),
                                        fieldWithPath("followeeCnt").type(NUMBER).description("팔로우 수")
                                )
                        )
                ));
    }

    @DisplayName("회원 수정")
    @Test
    void updateMemberTest() throws Exception {
        // Given
        MemberDto.Patch patchDto = new MemberDto.Patch("수정된 닉네임", "http://프사_주소", 150, 50);
        String content = gson.toJson(patchDto);

        Member updatedMember = Member.builder()
                .memberId(1L)
                .email("email@com")
                .nickname(patchDto.getNickname())
                .profileImageUrl(patchDto.getProfileImageUrl())
                .height(patchDto.getHeight())
                .weight(patchDto.getWeight())
                .build();
        given(memberService.updateMember(any())).willReturn(updatedMember);

        // When
        ResultActions actions = mockMvc.perform(
                patch("/members/{memberId}", updatedMember.getMemberId())
                        .header("Authorization", accessToken) // Access Token 전달
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        // Then
        actions.andExpect(status().isOk())
                .andDo(document(
                        "patch-member",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("memberId").description("회원번호")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("nickname").type(STRING).description("수정된 닉네임"),
                                        fieldWithPath("height").type(NUMBER).description("키"),
                                        fieldWithPath("weight").type(NUMBER).description("몸무게"),
                                        fieldWithPath("profileImageUrl").type(STRING).description("프로필 사진 주소")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("email").type(STRING).description("이메일"),
                                        fieldWithPath("nickname").type(STRING).description("닉네임"),
                                        fieldWithPath("profileImageUrl").type(STRING).description("프로필 사진 주소"),
                                        fieldWithPath("height").type(NUMBER).description("키"),
                                        fieldWithPath("weight").type(NUMBER).description("몸무게"),
                                        fieldWithPath("followerCnt").type(NUMBER).description("팔로워 수"),
                                        fieldWithPath("followeeCnt").type(NUMBER).description("팔로우 수")
                                )
                        )
                ));

    }

    @DisplayName("회원 탈퇴")
    @Test
    void deleteMemberTest() throws Exception {
        // Given
        long memberId = 1L;

        // When
        ResultActions actions = mockMvc.perform(
                delete("/members/{memberId}", memberId)
                        .header("Authorization", accessToken)
        );

        // Then
        actions.andExpect(status().isNoContent())
                .andDo(document(
                        "delete-member",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("memberId").description("회원 번호")
                        )
                ));
    }
}