package com.lookatme.server.comment.repository;

import com.lookatme.server.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
//    @Query("select c from Comment c where c.post.postId = :postId")
//    List<Comment> findCommentsByPost(Long postId);
}
