package com.lookatme.server.board.repository;

import com.lookatme.server.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Integer> {
    Optional<Board> findById(int boardId);

    Optional<Board> findByBoardId(int boardId);
}
