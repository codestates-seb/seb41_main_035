package com.lookatme.server.member.controller;

import com.google.gson.Gson;
import com.lookatme.server.auth.controller.AuthController;
import com.lookatme.server.auth.dto.MemberPrincipal;
import com.lookatme.server.auth.jwt.JwtTokenizer;
import com.lookatme.server.auth.jwt.RedisRepository;
import com.lookatme.server.auth.service.AuthService;
import com.lookatme.server.config.CustomTestConfiguration;
import com.lookatme.server.member.dto.MemberDto;
import com.lookatme.server.member.entity.Follow;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
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

    @DisplayName("팔로우 테스트")
    @Test
    void followTest() throws Exception {
        // Given
        willDoNothing().given(memberService).followMember(anyString(), anyString());

        // When
        ResultActions actions = mockMvc.perform(
                post("/members/follow")
                        .header("Authorization", accessToken)
                        .param("type", "up")
                        .param("nickname", "opponent_nickname")
        );

        // Then
        actions
                .andExpect(status().isOk())
                .andDo(document(
                                "post-member-follow",
                                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                                requestParameters(
                                        parameterWithName("type").description("팔로우 기능 구분(up/down)"),
                                        parameterWithName("nickname").description("상대방 닉네임")
                                )
                        )
                );
    }

    @DisplayName("회원 목록 조회 테스트")
    @Test
    void getMembersTest() throws Exception {
        // Given
        MemberPrincipal memberPrincipal = new MemberPrincipal(
                1L,
                "email@com",
                Member.OauthPlatform.NONE,
                "email@com/NONE");
        int page = 1;
        int size = 10;
        String tab = "followee";

        List<Member> memberList = List.of(savedMember);
        savedMember.getFollowers().add(new Follow(null, null));

        PageRequest pageRequest = PageRequest.of(page - 1, size);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), memberList.size());
        Page<Member> memberPage = new PageImpl<>(memberList.subList(start, end), pageRequest, memberList.size());

        given(
                memberService.findFollowers(
                        memberPrincipal.getMemberUniqueKey(),
                        tab,
                        page - 1,
                        size)
        ).willReturn(memberPage);


        // When
        ResultActions actions = mockMvc.perform(
                get("/members")
                        .param("page", Integer.toString(page))
                        .param("size", Integer.toString(size))
                        .param("tab", tab
                        )
                        .header("Authorization", accessToken)
        );

        // Then
        actions
                .andExpect(status().isOk())
                .andDo(document(
                        "get-members",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        requestParameters(
                                List.of(
                                        parameterWithName("page").description("페이지"),
                                        parameterWithName("size").description("페이지 당 데이터 개수"),
                                        parameterWithName("tab").description("필터 기준(followee/follower)")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data[].email").type(STRING).description("이메일"),
                                        fieldWithPath("data[].nickname").type(STRING).description("닉네임"),
                                        fieldWithPath("data[].profileImageUrl").type(STRING).description("프로필 사진 주소"),
                                        fieldWithPath("data[].height").type(NUMBER).description("키"),
                                        fieldWithPath("data[].weight").type(NUMBER).description("몸무게"),
                                        fieldWithPath("data[].followerCnt").type(NUMBER).description("팔로워 수"),
                                        fieldWithPath("data[].followeeCnt").type(NUMBER).description("팔로우 수"),
                                        fieldWithPath("pageInfoDto.page").type(NUMBER).description("페이지"),
                                        fieldWithPath("pageInfoDto.size").type(NUMBER).description("페이지 당 데이터 개수"),
                                        fieldWithPath("pageInfoDto.totalElements").type(NUMBER).description("전체 데이터 개수"),
                                        fieldWithPath("pageInfoDto.totalPages").type(NUMBER).description("전체 페이지")

                                )
                        )
                ));
    }
}