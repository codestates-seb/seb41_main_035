package com.lookatme.server.board.service;

import com.lookatme.server.board.dto.BoardPatchDto;
import com.lookatme.server.board.dto.BoardPostDto;
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

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class BoardService {

    private final BoardMapper mapper;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final FileService fileService;
    private final FollowService followService;
    private final RentalService rentalService;
    private final ProductService productService;

    public Board createBoard(BoardPostDto post, long memberId) throws IOException {
        // 1. 게시글 저장
        Board board = mapper.boardPostToBoard(post);
        board.setMember(findMember(memberId));
        String userImageUrl = fileService.upload(post.getUserImage(), FileDirectory.post);
        board.setUserImage(userImageUrl);
        Board savedBoard = boardRepository.save(board);

        // 2. 게시글 상품 저장
        for (ProductPostDto postProduct : post.getProducts()) {
            String itemImageUrl = fileService.upload(postProduct.getProductImage(), FileDirectory.item);
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


    public Board updateBoard(BoardPatchDto patch, int boardId, long memberId) throws IOException {
        Board savedBoard = findBoard(boardId);
        // 로그인 한 작성자가 아니면 수정할 수 없음
        if (savedBoard.getMember().getMemberId() != memberId) {
            throw new ErrorLogicException(ErrorCode.FORBIDDEN);
        }
        // userImage가 없으면 수정하지 않음
        String userImageUrl = savedBoard.getUserImage();
        if (patch.getUserImage() != null) {
            userImageUrl = fileService.upload(patch.getUserImage(), FileDirectory.post);
        }
        savedBoard.updateBoard(userImageUrl, patch.getContent());

        Set<Integer> savedProductIdSet = savedBoard.getBoardProducts().stream()
                .map(BoardProduct::getProductId)
                .collect(Collectors.toSet());

        if (patch.getProducts() != null) {
            for (ProductPatchDto product : patch.getProducts()) {
                // id가 없는 상품이면 신규 생성
                Product savedProduct;
                if (product.getProductId() == null) {
                    String itemImageUrl = fileService.upload(product.getProductImage(), FileDirectory.item);
                    savedProduct = productService.createProduct(product, itemImageUrl);
                    savedBoard.getBoardProducts().add(new BoardProduct(savedBoard, savedProduct));
                    if (product.isRental()) {
                        Rental rental = rentalService.createRental(
                                memberId,
                                savedProduct.getProductId(),
                                savedBoard,
                                product.getSize(),
                                product.getRentalPrice()
                        );
                        savedProduct.getRentals().add(rental);
                    }
                } else {
                    savedProduct = productService.updateProduct(product);
                    savedProductIdSet.remove(product.getProductId());
                }

                // 렌탈 정보가 있으면 업데이트
                if (product.getRentalId() == null) {
                    if(product.isRental()) {
                        Rental rental = rentalService.createRental(
                                memberId,
                                savedProduct.getProductId(),
                                savedBoard,
                                product.getSize(),
                                product.getRentalPrice()
                        );
                        savedProduct.getRentals().add(rental);
                    }
                } else {
                    rentalService.updateRental(
                            new RentalPatchDto(
                                    product.getRentalId(),
                                    product.getRentalPrice(),
                                    product.getSize(),
                                    product.isRental()
                            )
                    );
                }
            }
        }
        // id가 오지 않은 상품은 삭제
        for (Integer productId : savedProductIdSet) {
            productService.deleteProduct(productId);
        }
        return savedBoard;
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
    public Page<Board> findBoards(int page, int size) {
        return findBoards(page, size, -1); // 팔로우 유무 체크하지 않음
    }

    public Page<Board> findBoards(int page, int size, long memberId) {
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
            return new PageImpl<>(boardPage.getContent(), boardPage.getPageable(), boardPage.getTotalElements());
        } else {
            return boardPage;
        }
    }

    private Member findMember(long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ErrorLogicException(ErrorCode.MEMBER_NOT_FOUND));
    }
}
