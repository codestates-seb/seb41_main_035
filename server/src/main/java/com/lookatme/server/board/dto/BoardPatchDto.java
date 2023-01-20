package com.lookatme.server.board.dto;

import lombok.Getter;

@Getter
public class BoardPatchDto {

    // 수정할 수 있는 내용이 어떤게 있을지 이야기 해보자
    private int boardId;

    private String content;

    private String userImage;

    public void setBoardId(int boardId) {
        this.boardId = boardId;
    }
}
