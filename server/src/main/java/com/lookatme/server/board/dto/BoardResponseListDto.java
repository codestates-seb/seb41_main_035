package com.lookatme.server.board.dto;

import com.lookatme.server.member.dto.MemberDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BoardResponseListDto {

    private int boardId;

    private String userImage;

    private String content;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private int likeCnt;

    private MemberDto.SimpleResponse member;
}
