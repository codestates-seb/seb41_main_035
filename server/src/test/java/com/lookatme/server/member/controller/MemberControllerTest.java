package com.lookatme.server.member.controller;

import com.google.gson.Gson;
import com.lookatme.server.auth.controller.AuthController;
import com.lookatme.server.auth.jwt.JwtTokenizer;
import com.lookatme.server.auth.jwt.RedisRepository;
import com.lookatme.server.auth.service.AuthService;
import com.lookatme.server.auth.utils.MemberAuthorityUtils;
import com.lookatme.server.config.SecurityConfiguration;
import com.lookatme.server.member.dto.MemberDto;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.mapper.MemberMapperImpl;
import com.lookatme.server.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({
        SecurityConfiguration.class,
        MemberAuthorityUtils.class,
        JwtTokenizer.class,
        MemberMapperImpl.class
})
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest({MemberController.class, AuthController.class})
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private AuthService authService;

    @MockBean
    private RedisRepository redisRepository;

    @Autowired
    private Gson gson;

    @Test // TODO: AuthControllerTest로 분리 필요
    void registerMemberTest() throws Exception {
        // Given
        MemberDto.Post postDto = new MemberDto.Post(
                "email@com",
                "pwd123!@#",
                "닉네임",
                Member.OauthPlatform.NONE,
                180, 70);
        String content = gson.toJson(postDto);

        Member savedMember = createTestMember(1L, "email@com", "닉네임");
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

    @Test
    void getMemberTest() throws Exception {
        // Given
        Member savedMember = createTestMember(1L, "email@com", "닉네임");
        given(memberService.findMember(savedMember.getMemberId())).willReturn(savedMember);

        // When
        ResultActions actions = mockMvc.perform(
                get("/members/{memberId}", savedMember.getMemberId())
                        .accept(MediaType.APPLICATION_JSON)
        );

        // Then
        actions.andExpect(status().isOk());
    }

    @Test
    @WithMockUser()
    void updateMemberTest() throws Exception {
        // Given
        MemberDto.Patch patchDto = new MemberDto.Patch("수정된 닉네임", 150, 50);
        String content = gson.toJson(patchDto);

        Member updatedMember = Member.builder()
                .memberId(1L)
                .email("email@com")
                .nickname(patchDto.getNickname())
                .profileImageUrl("http://프사링크")
                .height(patchDto.getHeight())
                .weight(patchDto.getWeight())
                .build();
        given(memberService.updateMember(any())).willReturn(updatedMember);

        // When
        ResultActions actions = mockMvc.perform(
                patch("/members/{memberId}", updatedMember.getMemberId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        // Then
        actions.andExpect(status().isOk());
    }

    @Test
    @WithMockUser()
    void deleteMemberTest() throws Exception {
        // Given
        long memberId = 1L;

        // When
        ResultActions actions = mockMvc.perform(
                delete("/members/{memberId}", memberId)
        );

        // Then
        actions.andExpect(status().isNoContent());
    }

    private Member createTestMember(long memberId, String email, String nickname) {
        return Member.builder()
                .memberId(memberId)
                .email(email)
                .nickname(nickname)
                .profileImageUrl("http://프사링크")
                .height(180)
                .weight(70)
                .build();
    }
}