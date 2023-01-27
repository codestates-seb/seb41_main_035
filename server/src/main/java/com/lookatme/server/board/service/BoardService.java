package com.lookatme.server.board.service;

import com.lookatme.server.board.dto.BoardPatchDto;
import com.lookatme.server.board.dto.BoardPostDto;
import com.lookatme.server.board.dto.BoardResponseDto;
import com.lookatme.server.board.entity.Board;
import com.lookatme.server.board.mapper.BoardMapper;
import com.lookatme.server.board.repository.BoardRepository;
import com.lookatme.server.board.entity.BoardProduct;
import com.lookatme.server.exception.ErrorCode;
import com.lookatme.server.exception.ErrorLogicException;
import com.lookatme.server.exception.board.BoardNotFoundException;
import com.lookatme.server.file.FileDirectory;
import com.lookatme.server.file.FileService;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.repository.MemberRepository;
import com.lookatme.server.member.service.FollowService;
import com.lookatme.server.product.dto.ProductPatchDto;
import com.lookatme.server.product.dto.ProductPostDto;
import com.lookatme.server.product.entity.Product;
import com.lookatme.server.product.repository.ProductRepository;
import com.lookatme.server.product.service.ProductService;
import com.lookatme.server.rental.dto.RentalPatchDto;
import com.lookatme.server.rental.entity.Rental;
import com.lookatme.server.rental.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Transactional
@Service
public class BoardService {

    private final BoardMapper mapper;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final ProductRepository productRepository;
    private final FileService fileService;
    private final FollowService followService;
    private final RentalService rentalService;
    private final ProductService productService;

    public BoardResponseDto createBoard(BoardPostDto post, long memberId) {
        // 1. 게시글 저장
        Board board = mapper.boardPostToBoard(post);
        board.setMember(findMember(memberId));
        String userImageUrl = fileService.upload(post.getUserImage(), FileDirectory.post);
        board.setUserImage(userImageUrl);
        Board savedBoard = boardRepository.save(board);

        if (post.getProducts() != null) {
            for (ProductPostDto postProduct : post.getProducts()) {
                // 2. 게시글 상품 저장
                String itemImageUrl = fileService.upload(postProduct.getProductImage(), FileDirectory.item);
                Product savedProduct = productService.createProduct(postProduct, itemImageUrl);
                BoardProduct boardProduct = BoardProduct.builder()
                        .board(board)
                        .product(savedProduct)
                        .price(postProduct.getPrice())
                        .size(postProduct.getSize())
                        .link(postProduct.getLink()).build();
                board.getBoardProducts().add(boardProduct);

                // 3. 렌탈 정보 저장
                Rental rental = rentalService.createRental(
                        memberId,
                        savedProduct.getProductId(),
                        board,
                        postProduct.getSize(),
                        postProduct.getRentalPrice(),
                        postProduct.isRental()
                );
                savedProduct.getRentals().add(rental);
            }
        }
        return mapper.boardToBoardResponse(savedBoard);
    }

    public BoardResponseDto updateBoard(BoardPatchDto patch, int boardId, long memberId) {
        Board savedBoard = findBoard(boardId);
        // 로그인 한 작성자가 아니면 수정할 수 없음
        if (savedBoard.getMember().getMemberId() != memberId) {
            throw new ErrorLogicException(ErrorCode.FORBIDDEN);
        }
        if (patch.getProducts() != null) {
            for (ProductPatchDto product : patch.getProducts()) {
                productService.updateProduct(product);
                for (BoardProduct boardProduct : savedBoard.getBoardProducts()) {
                    if (boardProduct.getProduct().getProductId() == product.getProductId()) {
                        boardProduct.updateProductInfo(
                                product.getLink(),
                                product.getSize(),
                                product.getPrice()
                        );
                        break;
                    }
                }
                rentalService.updateRental(
                        savedBoard.getBoardId(),
                        product.getProductId(),
                        new RentalPatchDto(
                                product.getRentalPrice(),
                                product.getSize(),
                                product.isRental()
                        )
                );
            }
        }
        // userImage가 없으면 수정하지 않음
        String userImageUrl = savedBoard.getUserImage();
        if (patch.getUserImage() != null) {
            userImageUrl = fileService.upload(patch.getUserImage(), FileDirectory.post);
        }
        savedBoard.updateBoard(userImageUrl, patch.getContent());
        return mapper.boardToBoardResponse(savedBoard);
    }

    public void deleteBoard(int boardId) {
        boardRepository.delete(findBoard(boardId));
    }

    public void deleteBoards() {
        boardRepository.deleteAll();
    }

    @Transactional(readOnly = true)
    public Board findBoard(int boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(BoardNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Board findBoard(int boardId, long loginMemberId) {
        Board board = findBoard(boardId);
        Member writer = board.getMember();
        if (loginMemberId != -1) {
            if (followService.isFollowee(loginMemberId, writer.getMemberId())) {
                writer.setStatusToFollowingMember();
            }
        }
        return board;
    }

    @Transactional(readOnly = true)
    public Page<BoardResponseDto> findBoards(int page, int size) {
        return findBoards(page, size, -1); // 팔로우 유무 체크하지 않음
    }

    @Transactional(readOnly = true)
    public Page<BoardResponseDto> findBoards(int page, int size, long memberId) {
        Page<Board> boardPage = boardRepository.findAll(PageRequest.of(page, size, Sort.by("createdDate").descending()));
        if (memberId != -1) {
            Set<Long> followMemberIdList = followService.getFollowMemberIdSet(memberId); // 현재 로그인 한 회원이 팔로우 중인 회원 id list
            boardPage.getContent().stream()
                    .map(Board::getMember)
                    .forEach(member -> {
                        if (followMemberIdList.contains(member.getMemberId())) {
                            member.setStatusToFollowingMember();
                        }
                    });
        }
        List<BoardResponseDto> response = mapper.boardsToBoardResponseDtos(boardPage.getContent());
        return new PageImpl<>(response, boardPage.getPageable(), boardPage.getTotalElements());
    }

    private Member findMember(long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ErrorLogicException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private Product findProduct(long productId) {
        return productRepository.findByProductId(productId)
                .orElseThrow(() -> new ErrorLogicException(ErrorCode.PRODUCT_NOT_FOUND));
    }
}
