package com.lookatme.server.board.mapper;

import com.lookatme.server.board.dto.BoardPatchDto;
import com.lookatme.server.board.dto.BoardPostDto;
import com.lookatme.server.board.dto.BoardResponseDto;
import com.lookatme.server.board.entity.Board;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface BoardMapper {
    Board boardPostToBoard(BoardPostDto post);
    Board boardPatchToBoard(BoardPatchDto patch);
    BoardResponseDto boardToBoardResponse(Board board);
    List<BoardResponseDto> boardsToBoardResponseDtos(List<Board> boards);
}
