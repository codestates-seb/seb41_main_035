package com.lookatme.server.board.controller;

import com.lookatme.server.auth.dto.MemberPrincipal;
import com.lookatme.server.board.dto.BoardPostDto;
import com.lookatme.server.common.dto.MultiResponseDto;
import com.lookatme.server.board.dto.BoardPatchDto;
import com.lookatme.server.board.entity.Board;
import com.lookatme.server.board.mapper.BoardMapper;
import com.lookatme.server.board.service.BoardService;
import com.lookatme.server.file.service.FileService;
import com.lookatme.server.product.dto.ProductPostDto;
import com.lookatme.server.product.entity.Product;
import com.lookatme.server.product.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;
    private final ProductService productService;
    private final FileService fileService;
    private final BoardMapper boardMapper;

    public BoardController(BoardService boardService, ProductService productService, FileService fileService, BoardMapper boardMapper) {
        this.boardService = boardService;
        this.productService = productService;
        this.fileService = fileService;
        this.boardMapper = boardMapper;
    }

    //    @PostMapping
//    public ResponseEntity postBoard(@Valid @RequestBody BoardPostDto post) {
//        Board createBoard = boardService.crea
//        BoardResponseDto boardResponseDto = boardMapper.BOARD(createBoard);
//        return new ResponseEntity<>(boardMapper.boardToBoardResponse(createBoard), HttpStatus.CREATED);
//    }

    @PostMapping
    public ResponseEntity postBoard(@Valid @RequestBody BoardPostDto post,
                                    @AuthenticationPrincipal MemberPrincipal memberPrincipal) {

        Board board = boardMapper.boardPostToBoard(post);

        List<Product> products = new ArrayList<>();
        for (ProductPostDto product : post.getProducts()) {
            products.add(productService.createProduct(product));
        }

        boardService.createBoard(board, memberPrincipal.getMemberId(), products);


        return new ResponseEntity(HttpStatus.OK);
    }

    @PatchMapping("/board-Id")
    public ResponseEntity patchBoard(@PathVariable("board-Id") int boardId,
                                     @Valid @RequestBody BoardPatchDto patch) {
        Board board = boardService.updateBoard(boardMapper.boardPatchToBoard(patch));

        return new ResponseEntity<>(boardMapper.boardToBoardResponse(board), HttpStatus.OK);
    }

    @GetMapping("/board-Id")
    public ResponseEntity getBoard(@PathVariable("board-Id") int boardId) {
        Board board = boardService.findBoard(boardId);

        return new ResponseEntity<>(boardMapper.boardToBoardResponse(board), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getBoards(@Positive @RequestParam int page,
                                    @Positive @RequestParam int size) {
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
