package com.lookatme.server.board.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardResponseDto {

    private int boardId;

//  첨부사진 주소
    private String userImage;

    private String content;

    private String createdAt;

    private String modifiedAt;

    private int like;

}
