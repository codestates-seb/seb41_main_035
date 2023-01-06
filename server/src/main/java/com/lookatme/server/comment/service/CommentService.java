package com.lookatme.server.comment.service;

import com.lookatme.server.comment.dto.CommentPatchDto;
import com.lookatme.server.comment.dto.CommentPostDto;
import com.lookatme.server.comment.entity.Comment;
import com.lookatme.server.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public Comment createComment(final CommentPostDto commentPostDto) {
        //Member

        Comment comment = commentPostDto.toComment();

        return commentRepository.save(comment);
    }

    public Comment findValidateComment(final Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow();
    }

    public Comment editComment(final Long commentId,
                               final CommentPatchDto commentPatchDto) {
        Comment findComment = findValidateComment(commentId);
        Optional.ofNullable(commentPatchDto.getContent())
                .ifPresent(content -> findComment.changeContent(content));

        return commentRepository.save(findComment);
    }

    public Comment findComment(final Long commentId) {
        return findValidateComment(commentId);
    }

//    public List<Comment> findComments(final Long postId) {
//        return commentRepository.findCommentsByPost(postId);
//
//    }

    public void deleteComment(final Long commentId) {
        Comment findComment = findValidateComment(commentId);
        commentRepository.deleteById(commentId);
    }
}
