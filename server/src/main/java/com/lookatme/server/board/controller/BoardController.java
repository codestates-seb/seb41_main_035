package com.lookatme.server.board.controller;

import com.lookatme.server.auth.dto.MemberPrincipal;
import com.lookatme.server.board.dto.BoardPostDto;
import com.lookatme.server.common.dto.MultiResponseDto;
import com.lookatme.server.board.dto.BoardPatchDto;
import com.lookatme.server.board.entity.Board;
import com.lookatme.server.board.mapper.BoardMapper;
import com.lookatme.server.board.service.BoardService;
import com.lookatme.server.file.service.FileService;
import com.lookatme.server.member.entity.Follow;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.product.dto.ProductPostDto;
import com.lookatme.server.product.entity.Product;
import com.lookatme.server.product.service.ProductService;
import com.lookatme.server.rental.service.RentalService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;
    private final ProductService productService;
    private final RentalService rentalService;
    private final FileService fileService;
    private final BoardMapper boardMapper;

    public BoardController(BoardService boardService, ProductService productService, RentalService rentalService, FileService fileService, BoardMapper boardMapper) {
        this.boardService = boardService;
        this.productService = productService;
        this.rentalService = rentalService;
        this.fileService = fileService;
        this.boardMapper = boardMapper;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity postBoard(@Valid @RequestPart(name = "request", required = false) BoardPostDto post,
                                    @RequestPart String content,
                                    @RequestPart(name = "userImage") MultipartFile userImage,
//                                    @RequestPart(name = "productImages") MultipartFile[] productImages,
                                    @AuthenticationPrincipal MemberPrincipal memberPrincipal) throws IOException {
//        Board board = boardMapper.boardPostToBoard(post);

        Board board = new Board();
        board.setContent(content);

        // 1. 게시글 사진 업로드
        String userImageUrl = fileService.upload(userImage, "post");
        board.setUserImage(userImageUrl);

        List<Product> products = new ArrayList<>();
//        List<ProductPostDto> postProducts = post.getProducts();
//        for(int i = 0; i < postProducts.size(); i++) {
//            // 2. 상품 사진 업로드 TODO: 상품이 이미 등록되어 있으면 생략해도 됨
////            String itemImageUrl = fileService.upload(productImages[i], "item");
//            String itemImageUrl = "https://cdn.imweb.me/upload/S20211026228188315d8e6/590e88b6bb53b.jpg";
//            ProductPostDto postDto = postProducts.get(i);
//            postDto.setProductImage(itemImageUrl);
//
//            // 3. 상품 등록
//            Product product = productService.createProduct(postDto);
//
//            if(postDto.isRental()) {
//                // 4. 렌탈 등록
//                rentalService.createRental(
//                        memberPrincipal.getMemberId(),
//                        product.getProductId(),
//                        postDto.getSize(),
//                        postDto.getRentalPrice()
//                );
//            }
//            products.add(product);
//        }

        // 5. 게시글 등록
        boardService.createBoard(board, memberPrincipal.getMemberId(), products);
        return new ResponseEntity(HttpStatus.OK);
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
