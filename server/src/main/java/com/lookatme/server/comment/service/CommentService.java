package com.lookatme.server.comment.service;

import com.lookatme.server.auth.dto.MemberPrincipal;
import com.lookatme.server.board.entity.Board;
import com.lookatme.server.board.repository.BoardRepository;
import com.lookatme.server.comment.dto.CommentPatchDto;
import com.lookatme.server.comment.dto.CommentPostDto;
import com.lookatme.server.comment.dto.CommentResponseDto;
import com.lookatme.server.comment.entity.Comment;
import com.lookatme.server.comment.repository.CommentRepository;
import com.lookatme.server.common.dto.MultiResponseDto;
import com.lookatme.server.exception.ErrorCode;
import com.lookatme.server.exception.ErrorLogicException;
import com.lookatme.server.exception.board.BoardNotFoundException;
import com.lookatme.server.exception.comment.CommentNotFoundException;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public CommentResponseDto createComment(final CommentPostDto commentPostDto,
                                            final MemberPrincipal memberPrincipal) {
        Member member = findValidateMember(memberPrincipal.getMemberId());
        Board board = findValidatePost(Integer.parseInt(commentPostDto.getBoardId()));

        Comment comment = commentPostDto.toComment();

        comment.addMember(member);
        comment.addBoard(board);

        return CommentResponseDto.of(commentRepository.save(comment));
    }

    private Member findValidateMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ErrorLogicException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public Comment findValidateComment(final Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException());
    }

    private Board findValidatePost(final int boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException());
    }

    @Transactional
    public CommentResponseDto editComment(final Long commentId,
                                          final CommentPatchDto commentPatchDto,
                                          final MemberPrincipal memberPrincipal) {
        Comment findComment = findValidateComment(commentId);
        checkValidateMember(memberPrincipal.getEmail(), findComment.getMember().getEmail());

        Optional.ofNullable(commentPatchDto.getContent())
                .ifPresent(content -> findComment.changeContent(content));

        return CommentResponseDto.of(findComment);
    }

    private void checkValidateMember(final String authMemberEmail,
                                     final String commentMemberEmail) {
        if (!authMemberEmail.equals(commentMemberEmail)) {
            throw new ErrorLogicException(ErrorCode.UNAUTHORIZED);
        }
    }

    public CommentResponseDto findComment(final Long commentId) {
        return CommentResponseDto.of(findValidateComment(commentId));
    }

    public MultiResponseDto getComments(final int boardId,
                                        final int page,
                                        final int size) {
        Page<Comment> comments = commentRepository.findCommentsByBoard(boardId,
                PageRequest.of(page, size, Sort.by("createdDate")));

        List<CommentResponseDto> commentResponseDtos = comments.stream()
                .map(comment -> CommentResponseDto.of(comment))
                .collect(Collectors.toList());

        return new MultiResponseDto<>(commentResponseDtos, comments);

    }

    @Transactional
    public void deleteComment(final Long commentId,
                              final MemberPrincipal memberPrincipal) {
        Comment findComment = findValidateComment(commentId);
        checkValidateMember(memberPrincipal.getEmail(), findComment.getMember().getEmail());
        commentRepository.deleteById(commentId);
    }
}
