package com.lookatme.server.board.dto;

import com.lookatme.server.member.dto.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardListResponseDto {

    private int boardId;

    private String userImage;

    private String content;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    private int likeCnt;

    private int commentCnt;

    private MemberDto.SimpleResponse member;
}
