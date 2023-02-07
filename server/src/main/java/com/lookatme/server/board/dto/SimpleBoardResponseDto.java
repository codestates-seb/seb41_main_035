package com.lookatme.server.board.dto;

import com.lookatme.server.board.entity.Board;
import com.lookatme.server.comment.dto.CommentResponseDtoV2;
import com.lookatme.server.member.dto.MemberDto;
import com.lookatme.server.product.dto.BoardProductsResponseDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleBoardResponseDto {

    private long boardId;

    private String userImage;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private int likeCnt;

    public static SimpleBoardResponseDto of(final Board board) {
        return SimpleBoardResponseDto.builder()
                .boardId(board.getBoardId())
                .userImage(board.getUserImage())
                .content(board.getContent())
                .createdAt(board.getCreatedDate())
                .modifiedAt(board.getUpdatedDate())
                .likeCnt(board.getLikeCnt())
                .build();
    }
}
