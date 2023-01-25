package com.lookatme.server.comment.controller;

import com.lookatme.server.auth.dto.MemberPrincipal;
import com.lookatme.server.comment.dto.CommentPatchDto;
import com.lookatme.server.comment.dto.CommentPostDto;
import com.lookatme.server.comment.service.CommentService;
import com.lookatme.server.common.dto.MultiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity postComment(@RequestBody @Valid CommentPostDto commentPostDto,
                                      @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        return new ResponseEntity<>(commentService.createComment(commentPostDto, memberPrincipal),
                HttpStatus.CREATED);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity patchComment(@PathVariable("commentId") Long commentId,
                                       @RequestBody @Valid CommentPatchDto commentPatchDto,
                                       @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        return new ResponseEntity<>(commentService.editComment(commentId, commentPatchDto, memberPrincipal),
                HttpStatus.OK);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity getComment(@PathVariable("commentId") Long commentId) {
        return new ResponseEntity<>(commentService.findComment(commentId), HttpStatus.OK);
    }

    @GetMapping("/board/{boardId}")
    public ResponseEntity<MultiResponseDto> getComments(@PathVariable("boardId") int boardId,
                                                        @RequestParam("page") final int page,
                                                        @RequestParam("size") final int size) {
        return new ResponseEntity<>(commentService.getComments(boardId, page - 1, size), HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity deleteComment(@PathVariable("commentId") Long commentId,
                                        @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        commentService.deleteComment(commentId, memberPrincipal);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
