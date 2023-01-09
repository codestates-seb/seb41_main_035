package com.lookatme.server.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lookatme.server.comment.controller.CommentController;
import com.lookatme.server.comment.dto.CommentPatchDto;
import com.lookatme.server.comment.dto.CommentPostDto;
import com.lookatme.server.comment.entity.Comment;
import com.lookatme.server.comment.service.CommentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

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

@WebMvcTest(CommentController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void postCommentTest() throws Exception {
        //given
        CommentPostDto commentPostDto = CommentPostDto.builder()
                .content("댓글 내용")
                .build();

        String content = objectMapper.writeValueAsString(commentPostDto);

        Comment comment = Comment.builder()
                .commentId(1L)
                .content(commentPostDto.getContent())
                .build();

        given(commentService.createComment(Mockito.any(CommentPostDto.class))).willReturn(comment);

        //when
        ResultActions actions =
                mockMvc.perform(post("/comment")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                );
        //then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.commentId").value(comment.getCommentId()))
                .andExpect(jsonPath("$.content").value(comment.getContent()))
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
                                        fieldWithPath("updatedDate").type(JsonFieldType.NULL).description("댓글 수정일")
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

        given(commentService.editComment(Mockito.anyLong(),Mockito.any(CommentPatchDto.class))).willReturn(comment);

        //when
        Long commentId = comment.getCommentId();
        ResultActions actions =
                mockMvc.perform(patch("/comment/{commentId}", commentId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                );
        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commentId").value(comment.getCommentId()))
                .andExpect(jsonPath("$.content").value(comment.getContent()))
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
                                        fieldWithPath("updatedDate").type(JsonFieldType.NULL).description("댓글 수정일")
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

        given(commentService.findComment(Mockito.anyLong())).willReturn(comment);

        //when
        ResultActions actions =
                mockMvc.perform(get("/comment/{commentId}", comment.getCommentId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                );
        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commentId").value(comment.getCommentId()))
                .andExpect(jsonPath("$.content").value(comment.getContent()))
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
                                        fieldWithPath("updatedDate").type(JsonFieldType.NULL).description("댓글 수정일")
                                )
                        )
                ));
    }

    @Test
    public void deleteCommentTest() throws Exception {
        //given
        Long commentId = 1L;
        doNothing().when(commentService).deleteComment(Mockito.anyLong());

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
