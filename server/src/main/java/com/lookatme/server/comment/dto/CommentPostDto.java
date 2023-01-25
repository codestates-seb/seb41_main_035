package com.lookatme.server.comment.dto;

import com.lookatme.server.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class CommentPostDto {
    @NotBlank
    private String boardId;

    @NotBlank
    private String content;

    private CommentPostDto() {

    }

    @Builder
    public CommentPostDto(final String boardId,
                          final String content) {
        this.boardId = boardId;
        this.content = content;
    }

    public Comment toComment() {
        return Comment.builder()
                .content(content)
                .build();
    }
}