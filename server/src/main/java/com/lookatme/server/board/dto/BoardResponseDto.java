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
public class BoardResponseDto {

    private int boardId;

    private String userImage;

    private String content;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private int likeCnt;

    private MemberDto.ResponseWithFollow member;

    private List<BoardProductsResponseDto> products;

    private List<CommentResponseDtoV2> comments;

    public static BoardResponseDto of(final Board board) {
        return BoardResponseDto.builder()
                .boardId(board.getBoardId())
                .userImage(board.getUserImage())
                .content(board.getContent())
                .createdDate(board.getCreatedDate())
                .updatedDate(board.getUpdatedDate())
                .likeCnt(board.getLikeCnt())
                .build();
    }
}
