package com.lookatme.server.board.service;

import com.lookatme.server.auth.dto.MemberPrincipal;
import com.lookatme.server.board.entity.Board;
import com.lookatme.server.board.repository.BoardRepository;
import com.lookatme.server.entity.BoardProduct;
import com.lookatme.server.exception.ErrorCode;
import com.lookatme.server.exception.ErrorLogicException;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.repository.MemberRepository;
import com.lookatme.server.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoardService {
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    public BoardService(MemberRepository memberRepository, BoardRepository boardRepository) {
        this.memberRepository = memberRepository;
        this.boardRepository = boardRepository;
    }

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

    public Board findBoard(int boardId) {

        return findExistedBoard(boardId);
    }

    public Page<Board> findBoards(int page, int size) {

        return boardRepository.findAll(PageRequest.of(page, size, Sort.by("createdAt").descending()));
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
