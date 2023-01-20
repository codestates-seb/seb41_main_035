package com.lookatme.server.board.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardResponseDto {

    private int boardId;

    private String userImage;

    private String content;

    private String createdAt;

    private String modifiedAt;

    private int likeCnt;

}
