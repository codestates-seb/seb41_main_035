package com.lookatme.server.comment.dto;

import com.lookatme.server.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private Long commentId;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @Builder
    public CommentResponseDto(final Long commentId,
                              final String content,
                              final LocalDateTime createdDate,
                              final LocalDateTime updatedDate) {
        this.commentId = commentId;
        this.content = content;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public static CommentResponseDto of(final Comment comment) {
        return CommentResponseDto.builder()
                .commentId(comment.getCommentId())
                .content(comment.getContent())
                .createdDate(comment.getCreatedDate())
                .updatedDate(comment.getUpdatedDate())
                .build();
    }
}
