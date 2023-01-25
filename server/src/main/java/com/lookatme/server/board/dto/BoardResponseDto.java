package com.lookatme.server.board.dto;

import com.lookatme.server.board.entity.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BoardResponseDto {

    private int boardId;

    private String userImage;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private int likeCnt;

    @Builder
    public BoardResponseDto(final int boardId,
                            final String userImage,
                            final String content,
                            final LocalDateTime createdAt,
                            final LocalDateTime modifiedAt,
                            final int likeCnt) {
        this.boardId = boardId;
        this.userImage = userImage;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.likeCnt = likeCnt;
    }
    public static BoardResponseDto of(final Board board) {
        return BoardResponseDto.builder()
                .boardId(board.getBoardId())
                .userImage(board.getUserImage())
                .content(board.getContent())
                .createdAt(board.getCreatedDate())
                .modifiedAt(board.getUpdatedDate())
                .likeCnt(board.getLikeCnt())
                .build();
    }

}
