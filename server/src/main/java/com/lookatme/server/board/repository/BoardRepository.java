package com.lookatme.server.board.repository;

import com.lookatme.server.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @EntityGraph(value = "board-entity-graph")
    Optional<Board> findById(long boardId);

    Optional<Board> findByBoardId(long boardId);

    @EntityGraph(value = "board-entity-graph")
    Page<Board> findAll(Pageable pageable);
}
