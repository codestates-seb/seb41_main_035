package com.lookatme.server.comment.dto;

import com.lookatme.server.comment.entity.Comment;
import com.lookatme.server.member.dto.MemberDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class CommentResponseDtoV2 {
    private Long commentId;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private MemberDto.SimpleResponse member;
}
