package com.lookatme.server.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lookatme.server.auth.dto.MemberPrincipal;
import com.lookatme.server.auth.jwt.JwtTokenizer;
import com.lookatme.server.auth.jwt.RedisRepository;
import com.lookatme.server.auth.utils.MemberAuthorityUtils;
import com.lookatme.server.comment.controller.CommentController;
import com.lookatme.server.comment.dto.CommentPatchDto;
import com.lookatme.server.comment.dto.CommentPostDto;
import com.lookatme.server.comment.entity.Comment;
import com.lookatme.server.comment.service.CommentService;
import com.lookatme.server.config.SecurityConfiguration;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({
        SecurityConfiguration.class,
        MemberAuthorityUtils.class,
        JwtTokenizer.class
})
@WebMvcTest(controllers = CommentController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfigurer.class)})
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CommentService commentService;
    @MockBean
    private RedisRepository redisRepository;
    @MockBean
    private MemberService memberService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void postCommentTest() throws Exception {
        //given
        CommentPostDto commentPostDto = CommentPostDto.builder()
                .content("댓글 내용")
                .build();

        Member member = Member.builder()
                .memberId(1L)
                .email("email@com")
                .nickname("댓글 작성자 닉네임")
                .profileImageUrl("댓글 작성자 프로필 사진 URL")
                .height(180)
                .weight(70)
                .build();

        String content = objectMapper.writeValueAsString(commentPostDto);

        Comment comment = Comment.builder()
                .commentId(1L)
                .content(commentPostDto.getContent())
                .member(member)
                .build();

        given(commentService.createComment(Mockito.any(CommentPostDto.class), Mockito.any())).willReturn(comment);

        //when
        ResultActions actions =
                mockMvc.perform(post("/comment")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(content)
                );
        //then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.commentId").value(comment.getCommentId()))
                .andExpect(jsonPath("$.content").value(comment.getContent()))
                .andExpect(jsonPath("$.nickname").value(comment.getMember().getNickname()))
                .andExpect(jsonPath("$.profileImageUrl").value(comment.getMember().getProfileImageUrl()))
                .andDo(document("create-comment",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        requestFields(
                                List.of(
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("commentId").type(JsonFieldType.NUMBER).description("댓글 식별자"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용"),
                                        fieldWithPath("createdDate").type(JsonFieldType.NULL).description("댓글 작성일"),
                                        fieldWithPath("updatedDate").type(JsonFieldType.NULL).description("댓글 수정일"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("댓글 작성자 닉네임"),
                                        fieldWithPath("profileImageUrl").type(JsonFieldType.STRING).description("댓글 작성자 프로필 사진")
                                )
                        )
                ));
    }

    @Test
    public void patchCommentTest() throws Exception {
        //given
        CommentPatchDto commentPatchDto = CommentPatchDto.builder()
                .content("댓글 내용")
                .build();

        String content = objectMapper.writeValueAsString(commentPatchDto);

        Comment comment = Comment.builder()
                .commentId(1L)
                .content(commentPatchDto.getContent())
                .build();

        Member member = Member.builder()
                .memberId(1L)
                .email("email@com")
                .nickname("댓글 작성자 닉네임")
                .profileImageUrl("댓글 작성자 프로필 사진 URL")
                .height(180)
                .weight(70)
                .build();

        comment.addMember(member);

        given(commentService.editComment(Mockito.anyLong(),Mockito.any(CommentPatchDto.class), Mockito.any())).willReturn(comment);

        //when
        Long commentId = comment.getCommentId();
        ResultActions actions =
                mockMvc.perform(patch("/comment/{commentId}", commentId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(content)
                );
        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commentId").value(comment.getCommentId()))
                .andExpect(jsonPath("$.content").value(comment.getContent()))
                .andExpect(jsonPath("$.nickname").value(comment.getMember().getNickname()))
                .andExpect(jsonPath("$.profileImageUrl").value(comment.getMember().getProfileImageUrl()))
                .andDo(document("patch-comment",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("commentId").description("댓글 식별자")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("수정 댓글 내용")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("commentId").type(JsonFieldType.NUMBER).description("댓글 식별자"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용"),
                                        fieldWithPath("createdDate").type(JsonFieldType.NULL).description("댓글 작성일"),
                                        fieldWithPath("updatedDate").type(JsonFieldType.NULL).description("댓글 수정일"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("댓글 작성자 닉네임"),
                                        fieldWithPath("profileImageUrl").type(JsonFieldType.STRING).description("댓글 작성자 프로필 사진")
                                )
                        )
                ));
    }

    @Test
    public void getCommentTest() throws Exception {
        //given
        Long commentId = 1L;
        Comment comment = Comment.builder()
                .commentId(commentId)
                .content("댓글 내용")
                .build();

        Member member = Member.builder()
                .memberId(1L)
                .email("email@com")
                .nickname("닉네임")
                .profileImageUrl("http://프사링크")
                .height(180)
                .weight(70)
                .build();

        comment.addMember(member);

        given(commentService.findComment(Mockito.anyLong())).willReturn(comment);

        //when
        ResultActions actions =
                mockMvc.perform(get("/comment/{commentId}", comment.getCommentId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                );
        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commentId").value(comment.getCommentId()))
                .andExpect(jsonPath("$.content").value(comment.getContent()))
                .andExpect(jsonPath("$.nickname").value(comment.getMember().getNickname()))
                .andExpect(jsonPath("$.profileImageUrl").value(comment.getMember().getProfileImageUrl()))
                .andDo(document("get-comment",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("commentId").description("댓글 식별자")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("commentId").type(JsonFieldType.NUMBER).description("댓글 식별자"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용"),
                                        fieldWithPath("createdDate").type(JsonFieldType.NULL).description("댓글 작성일"),
                                        fieldWithPath("updatedDate").type(JsonFieldType.NULL).description("댓글 수정일"),
                                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("댓글 작성자 닉네임"),
                                        fieldWithPath("profileImageUrl").type(JsonFieldType.STRING).description("댓글 작성자 프로필 사진")
                                )
                        )
                ));
    }

    @Test
    public void deleteCommentTest() throws Exception {
        //given
        Long commentId = 1L;
        doNothing().when(commentService).deleteComment(Mockito.anyLong(), Mockito.any(MemberPrincipal.class));

        //when
        ResultActions actions =
                mockMvc.perform(delete("/comment/{commentId}", commentId));
        //then
        actions
                .andExpect(status().isNoContent())
                .andDo(document("delete-comment",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("commentId").description("댓글 식별자")
                        )
                ));
    }
}
