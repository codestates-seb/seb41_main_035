package com.lookatme.server.board.controller;

import com.lookatme.server.auth.dto.MemberPrincipal;
import com.lookatme.server.board.dto.BoardPatchDto;
import com.lookatme.server.board.dto.BoardPostDto;
import com.lookatme.server.board.dto.BoardResponseDto;
import com.lookatme.server.board.service.BoardService;
import com.lookatme.server.common.dto.MultiResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/{board-Id}")
    public ResponseEntity<?> getBoard(@Positive @PathVariable("board-Id") long boardId,
                                      @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        long loginMemberId = memberPrincipal == null ? -1 : memberPrincipal.getMemberId();
        return new ResponseEntity<>(
                boardService.findBoard(boardId, loginMemberId),
                HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getBoards(@Positive @RequestParam(defaultValue = "1") int page,
                                       @Positive @RequestParam(defaultValue = "25") int size,
                                       @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        long loginMemberId = memberPrincipal == null ? -1 : memberPrincipal.getMemberId();
        Page<BoardResponseDto> findPosts = boardService.findBoards(page - 1, size, loginMemberId);
        List<BoardResponseDto> boards = findPosts.getContent();
        return new ResponseEntity<>(
                new MultiResponseDto<>(boards, findPosts), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> postBoard(BoardPostDto post,
                                       @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        return new ResponseEntity<>(
                boardService.createBoard(post, memberPrincipal.getMemberId()),
                HttpStatus.CREATED);
    }

    @PatchMapping("/{board-Id}")
    public ResponseEntity<?> patchBoard(BoardPatchDto patch,
                                        @PathVariable("board-Id") long boardId,
                                        @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        return new ResponseEntity<>(
                boardService.updateBoard(patch, boardId, memberPrincipal.getMemberId()),
                HttpStatus.OK);
    }

    @DeleteMapping("/{board-Id}")
    public ResponseEntity<?> deletePost(@Positive @PathVariable("board-Id") long boardId,
                                        @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        boardService.deleteBoard(boardId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{board-Id}/like")
    public ResponseEntity<?> likeBoard(@PathVariable("board-Id") long boardId,
                                       @AuthenticationPrincipal MemberPrincipal memberPrincipal){
        return new ResponseEntity<>(boardService.likeBoard(memberPrincipal,boardId), HttpStatus.OK);
    }

    @GetMapping("/search/product")
    public ResponseEntity<?> getBoardsByProductName(@RequestParam String productName,
                                                    @Positive @RequestParam(defaultValue = "1") int page,
                                                    @Positive @RequestParam(defaultValue = "25") int size,
                                                    @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        long loginMemberId = memberPrincipal == null ? -1 : memberPrincipal.getMemberId();

        Page<BoardResponseDto> findBoards = boardService.findBoardsByproductName(productName, page-1, size);
        List<BoardResponseDto> boards = findBoards.getContent();

        return new ResponseEntity<>(
                new MultiResponseDto<>(boards, findBoards), HttpStatus.OK);
    }

    @GetMapping("/search/category")
    public ResponseEntity<?> findBoardsByCategoryName(@RequestParam String category,
                                                      @Positive @RequestParam(defaultValue = "1") int page,
                                                      @Positive @RequestParam(defaultValue = "25") int size,
                                                      @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        long loginMemberId = memberPrincipal == null ? -1 : memberPrincipal.getMemberId();

        Page<BoardResponseDto> findBoards = boardService.findBoardsByCategoryName(category, page-1, size);
        List<BoardResponseDto> boards = findBoards.getContent();

        return new ResponseEntity<>(
                new MultiResponseDto<>(boards, findBoards), HttpStatus.OK);
    }

    @GetMapping("/search/available")
    public ResponseEntity<?> findBoardsByCategoryName(@Positive @RequestParam(defaultValue = "1") int page,
                                                      @Positive @RequestParam(defaultValue = "25") int size,
                                                      @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        long loginMemberId = memberPrincipal == null ? -1 : memberPrincipal.getMemberId();

        Page<BoardResponseDto> findBoards = boardService.findBoardsByRentalAvailable(page-1, size);
        List<BoardResponseDto> boards = findBoards.getContent();

        return new ResponseEntity<>(
                new MultiResponseDto<>(boards, findBoards), HttpStatus.OK);
    }
}
