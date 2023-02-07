package com.lookatme.server.board.mapper;

import com.lookatme.server.board.dto.BoardPatchDto;
import com.lookatme.server.board.dto.BoardPostDto;
import com.lookatme.server.board.dto.BoardResponseDto;
import com.lookatme.server.board.entity.Board;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface BoardMapper {

    @Mapping(target = "userImage", ignore = true)
    Board boardPostToBoard(BoardPostDto post);

    @Mapping(target = "userImage", ignore = true)
    Board boardPatchToBoard(BoardPatchDto patch);

    @Mapping(target = "products", source = "boardProducts")
    BoardResponseDto boardToBoardResponse(Board board);

    List<BoardResponseDto> boardsToBoardResponseDtos(List<Board> boards);
}
