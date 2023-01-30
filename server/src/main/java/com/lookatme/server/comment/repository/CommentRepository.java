package com.lookatme.server.comment.repository;

import com.lookatme.server.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c where board_id = :board_id")
    Page<Comment> findCommentsByBoard(@Param("board_id") long boardId, Pageable pageable);
}