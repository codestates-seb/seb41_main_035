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
    private String nickname;
    private String profileImageUrl;

    @Builder
    public CommentResponseDto(final Long commentId,
                              final String content,
                              final LocalDateTime createdDate,
                              final LocalDateTime updatedDate,
                              final String nickname,
                              final String profileImageUrl) {
        this.commentId = commentId;
        this.content = content;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }

    public static CommentResponseDto of(final Comment comment) {
        return CommentResponseDto.builder()
                .commentId(comment.getCommentId())
                .content(comment.getContent())
                .createdDate(comment.getCreatedDate())
                .updatedDate(comment.getUpdatedDate())
                .nickname(comment.getMember().getNickname())
                .profileImageUrl(comment.getMember().getProfileImageUrl())
                .build();
    }
}
