package com.lookatme.server.board.mapper;

import com.lookatme.server.board.dto.BoardPatchDto;
import com.lookatme.server.board.dto.BoardPostDto;
import com.lookatme.server.board.dto.BoardResponseDto;
import com.lookatme.server.board.entity.Board;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-01-20T16:24:05+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.16.1 (Eclipse Adoptium)"
)
@Component
public class BoardMapperImpl implements BoardMapper {

    @Override
    public Board boardPostToBoard(BoardPostDto post) {
        if ( post == null ) {
            return null;
        }

        Board board = new Board();

        board.setContent( post.getContent() );

        return board;
    }

    @Override
    public Board boardPatchToBoard(BoardPatchDto patch) {
        if ( patch == null ) {
            return null;
        }

        Board board = new Board();

        board.setBoardId( patch.getBoardId() );
        board.setUserImage( patch.getUserImage() );
        board.setContent( patch.getContent() );

        return board;
    }

    @Override
    public BoardResponseDto boardToBoardResponse(Board board) {
        if ( board == null ) {
            return null;
        }

        BoardResponseDto boardResponseDto = new BoardResponseDto();

        boardResponseDto.setBoardId( board.getBoardId() );
        boardResponseDto.setUserImage( board.getUserImage() );
        boardResponseDto.setContent( board.getContent() );
        boardResponseDto.setLikeCnt( board.getLikeCnt() );

        return boardResponseDto;
    }

    @Override
    public List<BoardResponseDto> boardsToBoardResponseDtos(List<Board> boards) {
        if ( boards == null ) {
            return null;
        }

        List<BoardResponseDto> list = new ArrayList<BoardResponseDto>( boards.size() );
        for ( Board board : boards ) {
            list.add( boardToBoardResponse( board ) );
        }

        return list;
    }
}
