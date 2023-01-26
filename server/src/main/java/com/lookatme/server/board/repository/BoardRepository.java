package com.lookatme.server.board.repository;

import com.lookatme.server.board.entity.Board;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Integer> {

    @EntityGraph(value = "board-entity-graph")
    Optional<Board> findById(int boardId);

    Optional<Board> findByBoardId(int boardId);
}
