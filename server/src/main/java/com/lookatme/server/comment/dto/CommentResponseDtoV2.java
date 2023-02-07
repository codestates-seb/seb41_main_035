package com.lookatme.server.comment.dto;

import com.lookatme.server.comment.entity.Comment;
import com.lookatme.server.member.dto.MemberDto;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDtoV2 {
    private Long commentId;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private MemberDto.SimpleResponse member;
}
