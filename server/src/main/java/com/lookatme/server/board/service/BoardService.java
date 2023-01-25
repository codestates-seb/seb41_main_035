package com.lookatme.server.board.service;

import com.lookatme.server.board.dto.BoardPostDto;
import com.lookatme.server.board.entity.Board;
import com.lookatme.server.board.repository.BoardRepository;
import com.lookatme.server.board.entity.BoardProduct;
import com.lookatme.server.exception.ErrorCode;
import com.lookatme.server.exception.ErrorLogicException;
import com.lookatme.server.exception.board.BoardNotFoundException;
import com.lookatme.server.file.service.FileService;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.repository.MemberRepository;
import com.lookatme.server.member.service.FollowService;
import com.lookatme.server.product.dto.ProductPostDto;
import com.lookatme.server.product.entity.Product;
import com.lookatme.server.product.service.ProductService;
import com.lookatme.server.rental.entity.Rental;
import com.lookatme.server.rental.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class BoardService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final FileService fileService;
    private final FollowService followService;
    private final RentalService rentalService;
    private final ProductService productService;

    public Board createBoard(BoardPostDto post, MultipartFile userImage, long memberId) throws IOException {
        // 1. 게시글 저장
        Board board = new Board();
        board.setMember(findMember(memberId));
        board.setContent(post.getContent());
//        String userImageUrl = fileService.upload(userImage, "post");
        String userImageUrl = "게시글 사진 주소";
        board.setUserImage(userImageUrl);
        Board savedBoard = boardRepository.save(board);

        // 2. 게시글 상품 저장
        for (ProductPostDto postProduct : post.getProducts()) {
            String itemImageUrl = "상품 사진 주소";
//            String itemImageUrl = fileService.upload(postProduct.getProductImage(), "item");
            Product product = productService.createProduct(postProduct, itemImageUrl);
            board.getBoardProducts().add(new BoardProduct(board, product));

            // 3. 렌탈 가능한 경우 렌탈 정보 저장
            if (postProduct.isRental()) {
                Rental rental = rentalService.createRental(
                        memberId,
                        product.getProductId(),
                        board,
                        postProduct.getSize(),
                        postProduct.getRentalPrice()
                );
                product.getRentals().add(rental);
            }
        }
        return savedBoard;
    }

    public Board updateBoard(Board board) {
        Board findBoard = findExistedBoard(board.getBoardId());

        Optional.ofNullable(board.getUserImage())
                .ifPresent(userImage -> findBoard.setUserImage(userImage));

        Optional.ofNullable(board.getContent())
                .ifPresent(content -> findBoard.setContent(content));

        Optional.ofNullable(board.getLikeCnt())
                .ifPresent(like -> findBoard.setLikeCnt(like));

        return boardRepository.save(findBoard);
    }

    public void deleteBoard(int boardId) {
        boardRepository.delete(findExistedBoard(boardId));
    }

    public void deleteBoards() {
        boardRepository.deleteAll();
    }

    public Board findBoard(int boardId, long loginMemberId) {
        Board board = findExistedBoard(boardId);
        Member writer = board.getMember();
        if(loginMemberId != -1) {
            if (followService.isFollowee(loginMemberId, writer.getMemberId())) {
                writer.setStatusToFollowingMember();
            }
        }
        return board;
    }

    public Page<Board> findBoards(int page, int size) {
        return boardRepository.findAll(PageRequest.of(page, size, Sort.by("createdDate").descending()));
    }

    private Board findExistedBoard(int boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(BoardNotFoundException::new);
    }

    private Member findMember(long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ErrorLogicException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
