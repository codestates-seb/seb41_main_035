package com.lookatme.server.member.controller;

import com.google.gson.Gson;
import com.lookatme.server.auth.dto.MemberPrincipal;
import com.lookatme.server.auth.jwt.JwtTokenizer;
import com.lookatme.server.config.CustomTestConfiguration;
import com.lookatme.server.exception.ErrorCode;
import com.lookatme.server.file.FileDirectory;
import com.lookatme.server.file.FileService;
import com.lookatme.server.member.controller.MemberController;
import com.lookatme.server.member.dto.MemberDto;
import com.lookatme.server.member.entity.Account;
import com.lookatme.server.member.entity.Follow;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.entity.OauthPlatform;
import com.lookatme.server.member.mapper.MemberMapper;
import com.lookatme.server.member.mapper.MemberMapperImpl;
import com.lookatme.server.member.service.FollowService;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({
        CustomTestConfiguration.class,
        MemberMapperImpl.class,
        JwtTokenizer.class
})
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest({MemberController.class})
@AutoConfigureRestDocs
class MemberControllerTest {

    @MockBean
    private MemberService memberService;

    @MockBean
    private FileService fileService;

    @MockBean
    private FollowService followService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private MemberMapper mapper;

    private String accessToken;

    private Member savedMember;

    private MemberDto.Response savedMemberResponse;

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

        savedMemberResponse = mapper.memberToMemberResponse(savedMember);
        accessToken = jwtTokenizer.delegateAccessToken(savedMember);
    }

    @DisplayName("회원 조회")
    @Test
    void getMemberTest() throws Exception {
        // Given
        MemberDto.ResponseWithFollow response = mapper.memberToMemberResponseWithFollow(savedMember);
        given(memberService.findMember(savedMember.getMemberId())).willReturn(response);

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
                                        fieldWithPath("memberId").type(NUMBER).description("회원 번호"),
                                        fieldWithPath("email").type(STRING).description("이메일"),
                                        fieldWithPath("nickname").type(STRING).description("닉네임"),
                                        fieldWithPath("oauthPlatform").type(STRING).description("가입 플랫폼(NONE/GOOGLE)"),
                                        fieldWithPath("profileImageUrl").type(STRING).description("프로필 사진 주소"),
                                        fieldWithPath("height").type(NUMBER).description("키"),
                                        fieldWithPath("weight").type(NUMBER).description("몸무게"),
                                        fieldWithPath("followerCnt").type(NUMBER).description("팔로워 수"),
                                        fieldWithPath("followeeCnt").type(NUMBER).description("팔로우 수"),
                                        fieldWithPath("follow").type(BOOLEAN).description("팔로우 유무"),
                                        fieldWithPath("delete").type(BOOLEAN).description("회원 탈퇴 유무")
                                )
                        )
                ));
    }

    @DisplayName("회원 가입")
    @Test
    void registerMemberTest() throws Exception {
        // Given
        MemberDto.Post postDto = new MemberDto.Post(
                "email@com",
                "{noop}pwd123!@#",
                "닉네임",
                180, 70);

        String content = gson.toJson(postDto);
        given(memberService.registerMember(any(MemberDto.Post.class))).willReturn(savedMemberResponse);

        // When
        ResultActions actions = mockMvc.perform(
                post("/members/signup")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        // Then
        actions.andExpect(status().isCreated())
                .andDo(document(
                        "post-member",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        requestFields(
                                List.of(
                                        fieldWithPath("email").description("이메일"),
                                        fieldWithPath("password").description("비밀번호 (숫자/문자 포함 6자 이상)"),
                                        fieldWithPath("nickname").description("닉네임"),
                                        fieldWithPath("height").description("키(선택 사항)"),
                                        fieldWithPath("weight").description("몸무게(선택 사항")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("memberId").type(NUMBER).description("회원 번호"),
                                        fieldWithPath("email").type(STRING).description("이메일"),
                                        fieldWithPath("nickname").type(STRING).description("닉네임"),
                                        fieldWithPath("oauthPlatform").type(STRING).description("가입 플랫폼(NONE/GOOGLE)"),
                                        fieldWithPath("profileImageUrl").type(STRING).description("프로필 사진 주소"),
                                        fieldWithPath("height").type(NUMBER).description("키"),
                                        fieldWithPath("weight").type(NUMBER).description("몸무게"),
                                        fieldWithPath("followerCnt").type(NUMBER).description("팔로워 수"),
                                        fieldWithPath("followeeCnt").type(NUMBER).description("팔로우 수"),
                                        fieldWithPath("delete").type(BOOLEAN).description("회원 탈퇴 유무")
                                )
                        )
                ));

    }

    @DisplayName("회원 수정")
    @Test
    void updateMemberTest() throws Exception {
        // Given
        MemberDto.Patch patchDto = new MemberDto.Patch("수정된 닉네임", 150, 50);
        String content = gson.toJson(patchDto);

        Member updatedMember = Member.builder()
                .memberId(1L)
                .account(new Account("email@com", OauthPlatform.NONE))
                .nickname(patchDto.getNickname())
                .profileImageUrl("http://기본 프사 주소")
                .height(patchDto.getHeight())
                .weight(patchDto.getWeight())
                .build();
        MemberDto.Response updatedMemberResponse = mapper.memberToMemberResponse(updatedMember);
        given(memberService.updateMember(any(MemberDto.Patch.class), eq(1L))).willReturn(updatedMemberResponse);

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
                                        fieldWithPath("weight").type(NUMBER).description("몸무게")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("memberId").type(NUMBER).description("회원 번호"),
                                        fieldWithPath("email").type(STRING).description("이메일"),
                                        fieldWithPath("nickname").type(STRING).description("닉네임"),
                                        fieldWithPath("oauthPlatform").type(STRING).description("가입 플랫폼(NONE/GOOGLE)"),
                                        fieldWithPath("profileImageUrl").type(STRING).description("프로필 사진 주소"),
                                        fieldWithPath("height").type(NUMBER).description("키"),
                                        fieldWithPath("weight").type(NUMBER).description("몸무게"),
                                        fieldWithPath("followerCnt").type(NUMBER).description("팔로워 수"),
                                        fieldWithPath("followeeCnt").type(NUMBER).description("팔로우 수"),
                                        fieldWithPath("delete").type(BOOLEAN).description("회원 탈퇴 유무")
                                )
                        )
                ));

    }

    @DisplayName("회원 수정 실패 테스트 - 본인 계정 아님")
    @Test
    void updateMemberFailTest() throws Exception {
        // Given
        MemberDto.Patch patchDto = new MemberDto.Patch("수정된 닉네임", 150, 50);
        String content = gson.toJson(patchDto);

        // When
        ResultActions actions = mockMvc.perform(
                patch("/members/{memberId}", 2L)
                        .header("Authorization", accessToken) // Access Token 전달
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        // Then
        actions.andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.FORBIDDEN.name()))
                .andExpect(jsonPath("$.message").value(ErrorCode.FORBIDDEN.getValue()));
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

    @DisplayName("회원 탈퇴 실패 테스트 - 본인 계정 아님")
    @Test
    void deleteMemberFailTest() throws Exception {
        // When
        ResultActions actions = mockMvc.perform(
                delete("/members/{memberId}", 2L)
                        .header("Authorization", accessToken)
        );

        // Then
        actions.andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.FORBIDDEN.name()))
                .andExpect(jsonPath("$.message").value(ErrorCode.FORBIDDEN.getValue()));
    }


    @DisplayName("팔로우 테스트")
    @Test
    void followTest() throws Exception {
        // Given
        willDoNothing().given(followService).follow(anyLong(), anyLong());

        // When
        ResultActions actions = mockMvc.perform(
                post("/members/follow")
                        .header("Authorization", accessToken)
                        .param("type", "up")
                        .param("op", "1")
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
                                        parameterWithName("op").description("상대방 회원 번호")
                                )
                        )
                );
    }

    @DisplayName("회원 목록 조회 테스트")
    @Test
    void getMembersTest() throws Exception {
        // Given
        int page = 1;
        int size = 10;

        List<Member> memberList = List.of(savedMember);
        savedMember.getFollowers().add(new Follow(null, null));

        List<MemberDto.Response> responseList = mapper.memberListToMemberResponseList(memberList);

        PageRequest pageRequest = PageRequest.of(page - 1, size);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), memberList.size());
        Page<MemberDto.Response> memberPage = new PageImpl<>(responseList.subList(start, end), pageRequest, responseList.size());

        given(memberService.findMembers(page - 1, size)).willReturn(memberPage);


        // When
        ResultActions actions = mockMvc.perform(
                get("/members")
                        .param("page", Integer.toString(page))
                        .param("size", Integer.toString(size))
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
                                        parameterWithName("size").description("페이지 당 데이터 개수")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data[].memberId").type(NUMBER).description("회원 번호"),
                                        fieldWithPath("data[].email").type(STRING).description("이메일"),
                                        fieldWithPath("data[].nickname").type(STRING).description("닉네임"),
                                        fieldWithPath("data[].oauthPlatform").type(STRING).description("가입 플랫폼(NONE/GOOGLE)"),
                                        fieldWithPath("data[].profileImageUrl").type(STRING).description("프로필 사진 주소"),
                                        fieldWithPath("data[].height").type(NUMBER).description("키"),
                                        fieldWithPath("data[].weight").type(NUMBER).description("몸무게"),
                                        fieldWithPath("data[].followerCnt").type(NUMBER).description("팔로워 수"),
                                        fieldWithPath("data[].followeeCnt").type(NUMBER).description("팔로우 수"),
                                        fieldWithPath("data[].delete").type(BOOLEAN).description("회원 탈퇴 유무"),
                                        fieldWithPath("pageInfoDto.page").type(NUMBER).description("페이지"),
                                        fieldWithPath("pageInfoDto.size").type(NUMBER).description("페이지 당 데이터 개수"),
                                        fieldWithPath("pageInfoDto.totalElements").type(NUMBER).description("전체 데이터 개수"),
                                        fieldWithPath("pageInfoDto.totalPages").type(NUMBER).description("전체 페이지")

                                )
                        )
                ));
    }

    @DisplayName("회원 팔로우 목록 조회 테스트")
    @Test
    void getFollowMembersTest() throws Exception {
        // Given
        long memberId = 2L;
        String tab = "followee";
        int page = 1;
        int size = 10;

        List<Member> memberList = List.of(savedMember);
        savedMember.getFollowers().add(new Follow(null, null));
        List<MemberDto.Response> responseList = mapper.memberListToMemberResponseList(memberList);

        PageRequest pageRequest = PageRequest.of(page - 1, size);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), memberList.size());
        Page<MemberDto.Response> memberPage = new PageImpl<>(responseList.subList(start, end), pageRequest, responseList.size());

        given(followService.findFollows(memberId, tab, page - 1, size)).willReturn(memberPage);

        // When
        ResultActions actions = mockMvc.perform(
                get("/members/follow")
                        .param("page", Integer.toString(page))
                        .param("size", Integer.toString(size))
                        .param("memberId", Long.toString(memberId))
                        .param("tab", tab)
        );

        // Then
        actions
                .andExpect(status().isOk())
                .andDo(document(
                        "get-follow-members",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        requestParameters(
                                List.of(
                                        parameterWithName("page").description("페이지"),
                                        parameterWithName("size").description("페이지 당 데이터 개수"),
                                        parameterWithName("tab").description("조회 타입(followee, follower)"),
                                        parameterWithName("memberId").description("조회할 회원 번호")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data[].memberId").type(NUMBER).description("회원 번호"),
                                        fieldWithPath("data[].email").type(STRING).description("이메일"),
                                        fieldWithPath("data[].nickname").type(STRING).description("닉네임"),
                                        fieldWithPath("data[].oauthPlatform").type(STRING).description("가입 플랫폼(NONE/GOOGLE)"),
                                        fieldWithPath("data[].profileImageUrl").type(STRING).description("프로필 사진 주소"),
                                        fieldWithPath("data[].height").type(NUMBER).description("키"),
                                        fieldWithPath("data[].weight").type(NUMBER).description("몸무게"),
                                        fieldWithPath("data[].followerCnt").type(NUMBER).description("팔로워 수"),
                                        fieldWithPath("data[].followeeCnt").type(NUMBER).description("팔로우 수"),
                                        fieldWithPath("data[].delete").type(BOOLEAN).description("회원 탈퇴 유무"),
                                        fieldWithPath("pageInfoDto.page").type(NUMBER).description("페이지"),
                                        fieldWithPath("pageInfoDto.size").type(NUMBER).description("페이지 당 데이터 개수"),
                                        fieldWithPath("pageInfoDto.totalElements").type(NUMBER).description("전체 데이터 개수"),
                                        fieldWithPath("pageInfoDto.totalPages").type(NUMBER).description("전체 페이지")

                                )
                        )
                ));
    }

    @DisplayName("프로필 사진 등록 테스트")
    @Test
    void memberProfileImageTest() throws Exception {
        // Given
        MockMultipartFile image = new MockMultipartFile("image", "testImage.png", "image/png", "<<png data>>".getBytes());

        given(fileService.upload(any(MultipartFile.class), any(FileDirectory.class))).willReturn("새 프로필 사진 링크");
        given(memberService.setProfileImage(any(Account.class), anyString())).willReturn(savedMemberResponse);

        // When
        ResultActions actions = mockMvc.perform(
                multipart("/members/profile")
                        .file(image)
                        .header("Authorization", accessToken)
        );

        // Then
        actions.andExpect(status().isOk())
                .andDo(document(
                        "post-member-profile",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        requestParts(
                                partWithName("image").description("첨부 이미지(jpg/png) - 최대 3MB")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("memberId").type(NUMBER).description("회원 번호"),
                                        fieldWithPath("email").type(STRING).description("이메일"),
                                        fieldWithPath("nickname").type(STRING).description("닉네임"),
                                        fieldWithPath("oauthPlatform").type(STRING).description("가입 플랫폼(NONE/GOOGLE)"),
                                        fieldWithPath("profileImageUrl").type(STRING).description("변경된 프로필 사진 주소"),
                                        fieldWithPath("height").type(NUMBER).description("키"),
                                        fieldWithPath("weight").type(NUMBER).description("몸무게"),
                                        fieldWithPath("followerCnt").type(NUMBER).description("팔로워 수"),
                                        fieldWithPath("followeeCnt").type(NUMBER).description("팔로우 수"),
                                        fieldWithPath("delete").type(BOOLEAN).description("회원 탈퇴 유무")
                                )
                        )
                ));
    }
}