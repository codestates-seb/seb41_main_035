package com.lookatme.server.board.controller;

import com.lookatme.server.auth.dto.MemberPrincipal;
import com.lookatme.server.board.dto.BoardPostDto;
import com.lookatme.server.common.dto.MultiResponseDto;
import com.lookatme.server.board.dto.BoardPatchDto;
import com.lookatme.server.board.entity.Board;
import com.lookatme.server.board.mapper.BoardMapper;
import com.lookatme.server.board.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;
    private final BoardMapper boardMapper;

    public BoardController(BoardService boardService, BoardMapper boardMapper) {
        this.boardService = boardService;
        this.boardMapper = boardMapper;
    }

    @PostMapping
    public ResponseEntity postBoard(BoardPostDto post,
                                    MultipartFile userImage,
                                    @AuthenticationPrincipal MemberPrincipal memberPrincipal) throws Exception {
        Board board = boardService.createBoard(post, userImage, memberPrincipal.getMemberId());
        return new ResponseEntity<>(boardMapper.boardToBoardResponse(board), HttpStatus.OK);
    }

    @PatchMapping("/board-Id")
    public ResponseEntity patchBoard(@PathVariable("board-Id") int boardId,
                                     @Valid @RequestBody BoardPatchDto patch) {
        Board board = boardService.updateBoard(boardMapper.boardPatchToBoard(patch));
        return new ResponseEntity<>(boardMapper.boardToBoardResponse(board), HttpStatus.OK);
    }

    @GetMapping("/{board-Id}")
    public ResponseEntity getBoard(@PathVariable("board-Id") int boardId,
                                   @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        long loginMemberId = memberPrincipal == null ? -1 : memberPrincipal.getMemberId();
        Board board = boardService.findBoard(boardId, loginMemberId);
        return new ResponseEntity<>(boardMapper.boardToBoardResponse(board), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getBoards(@Positive @RequestParam(defaultValue = "1") int page,
                                    @Positive @RequestParam(defaultValue = "50") int size) {
        Page<Board> findPosts = boardService.findBoards(page - 1, size);
        List<Board> boards = findPosts.getContent();
        return new ResponseEntity<>(
                new MultiResponseDto<>(boardMapper.boardsToBoardResponseDtos(boards), findPosts), HttpStatus.OK);
    }

    @DeleteMapping("/{board-Id}")
    public ResponseEntity deletePost(@PathVariable("board-Id") int boardId) {
        boardService.deleteBoard(boardId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity deletePosts() {
        boardService.deleteBoards();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
