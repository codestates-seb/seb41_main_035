package com.lookatme.server.board.service;

import com.lookatme.server.board.dto.BoardPostDto;
import com.lookatme.server.board.entity.Board;
import com.lookatme.server.board.repository.BoardRepository;
import com.lookatme.server.board.entity.BoardProduct;
import com.lookatme.server.exception.ErrorCode;
import com.lookatme.server.exception.ErrorLogicException;
import com.lookatme.server.file.service.FileService;
import com.lookatme.server.member.entity.Follow;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.repository.MemberRepository;
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

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final FileService fileService;
    private final RentalService rentalService;
    private final ProductService productService;

    @Transactional
    public Board createBoard(Board board, long memberId, List<Product> products) {
        verifyExistBoard(board.getBoardId());
        Member member = findMember(memberId);
        board.setMember(member);

        List<BoardProduct> boardProducts = board.getBoardProducts();
        for (Product product : products) {
            boardProducts.add(new BoardProduct(board, product));
        }

        return boardRepository.save(board);
    }

    @Transactional
    public Board createBoardV2(BoardPostDto post, MultipartFile userImage, long memberId) throws IOException {
        Member member = findMember(memberId);
        Board board = new Board();
        board.setMember(member);
        board.setContent(post.getContent());

        // 1. 게시글 사진 업로드
        String userImageUrl = fileService.upload(userImage, "post");
        board.setUserImage(userImageUrl);
        Board savedBoard = boardRepository.save(board);

        List<ProductPostDto> postProducts = post.getProducts();

        for (int i = 0; i < postProducts.size(); i++) {
            // 2. 상품 사진 업로드
            ProductPostDto postDto = postProducts.get(i);
            String itemImageUrl = fileService.upload(postDto.getProductImage(), "item");

            // 3. 상품 등록
            Product product = productService.createProduct(postDto, itemImageUrl);
            board.getBoardProducts().add(new BoardProduct(board, product));

            if (postDto.isRental()) {
                // 4. 렌탈 등록
                Rental rental = rentalService.createRental(
                        memberId,
                        product.getProductId(),
                        board,
                        postDto.getSize(),
                        postDto.getRentalPrice()
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

    @Transactional(readOnly = true)
    public Board findBoard(int boardId, long loginMemberId) {
        Board board = boardRepository.findByBoardId(boardId)
                .orElseThrow(() -> new EntityNotFoundException("게시글 없음"));

        if (loginMemberId != -1) {
            Member loginMember = findMember(loginMemberId);
            Member member = board.getMember();
            Set<Follow> followers = member.getFollowers();
            for (Follow follower : followers) {
                if (follower.getFrom().equals(loginMember)) {
                    member.setFollowMemberStatus();
                    break;
                }
            }
        }
        return board;
    }

    public Page<Board> findBoards(int page, int size) {
        return boardRepository.findAll(PageRequest.of(page, size, Sort.by("createdDate").descending()));
    }

    private void verifyExistBoard(int boardId) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        if (optionalBoard.isPresent()) {
            throw new RuntimeException("Board_ALREADY_EXIST");
        }
    }

    private Board findExistedBoard(int boardId) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        return optionalBoard.orElseThrow(
                () -> new RuntimeException("Board_NOT_FOUND")
        );
    }

    private Member findMember(long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ErrorLogicException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
