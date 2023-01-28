package com.lookatme.server.board.dto;

import com.lookatme.server.comment.dto.CommentResponseDtoV2;
import com.lookatme.server.product.dto.BoardProductsResponseDto;
import com.lookatme.server.member.dto.MemberDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponseDto {

    private long boardId;

    private String userImage;

    private String content;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private int likeCnt;

    private MemberDto.ResponseWithFollow member;

    private List<BoardProductsResponseDto> products;

    private List<CommentResponseDtoV2> comments;
}
