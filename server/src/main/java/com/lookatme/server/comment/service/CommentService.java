package com.lookatme.server.comment.service;

import com.lookatme.server.auth.dto.MemberPrincipal;
import com.lookatme.server.comment.dto.CommentPatchDto;
import com.lookatme.server.comment.dto.CommentPostDto;
import com.lookatme.server.comment.entity.Comment;
import com.lookatme.server.comment.repository.CommentRepository;
import com.lookatme.server.exception.ErrorCode;
import com.lookatme.server.exception.ErrorLogicException;
import com.lookatme.server.exception.comment.CommentNotFoundException;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Comment createComment(final CommentPostDto commentPostDto,
                                 final MemberPrincipal memberPrincipal) {
        Member member = findValidateMember(memberPrincipal.getMemberId());

        Comment comment = commentPostDto.toComment();
        comment.addMember(member);

        return commentRepository.save(comment);
    }

    private Member findValidateMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ErrorLogicException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public Comment findValidateComment(final Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException());
    }

    @Transactional
    public Comment editComment(final Long commentId,
                               final CommentPatchDto commentPatchDto,
                               final MemberPrincipal memberPrincipal) {
        Comment findComment = findValidateComment(commentId);
        checkValidateMember(memberPrincipal.getEmail(), findComment.getMember().getEmail());

        Optional.ofNullable(commentPatchDto.getContent())
                .ifPresent(content -> findComment.changeContent(content));

        return findComment;
    }

    private void checkValidateMember(final String authMemberEmail,
                                     final String commentMemberEmail) {
        if (!authMemberEmail.equals(commentMemberEmail)) {
            throw new ErrorLogicException(ErrorCode.UNAUTHORIZED);
        }
    }
    public Comment findComment(final Long commentId) {
        return findValidateComment(commentId);
    }

//    public List<Comment> findComments(final Long postId) {
//        return commentRepository.findCommentsByPost(postId);
//
//    }

    @Transactional
    public void deleteComment(final Long commentId, final MemberPrincipal memberPrincipal) {
        Comment findComment = findValidateComment(commentId);
        checkValidateMember(memberPrincipal.getEmail(), findComment.getMember().getEmail());
        commentRepository.deleteById(commentId);
    }
}
