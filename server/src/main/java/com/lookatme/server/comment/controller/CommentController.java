package com.lookatme.server.comment.controller;

import com.lookatme.server.comment.dto.CommentPatchDto;
import com.lookatme.server.comment.dto.CommentPostDto;
import com.lookatme.server.comment.dto.CommentResponseDto;
import com.lookatme.server.comment.entity.Comment;
import com.lookatme.server.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity postComment(@RequestBody @Valid CommentPostDto commentPostDto){
        return new ResponseEntity<>(
                CommentResponseDto.of(commentService.createComment(commentPostDto)),
                HttpStatus.CREATED);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity patchComment(@PathVariable("commentId") Long commentId,
                                       @RequestBody @Valid CommentPatchDto commentPatchDto) {
        return new ResponseEntity<>(
                CommentResponseDto.of(commentService.editComment(commentId, commentPatchDto)),
                HttpStatus.OK);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity getComment(@PathVariable("commentId") Long commentId) {
        return new ResponseEntity<>(
                CommentResponseDto.of(commentService.findComment(commentId)),
                HttpStatus.OK);
    }

//    @GetMapping("/post/{postId}")
//    public ResponseEntity getComments(@PathVariable("postId") Long postId) {
//        List<Comment> comments = commentService.findComments(postId);
//        List<CommentResponseDto> commentResponseDtos = comments.stream()
//                .map(comment -> CommentResponseDto.of(comment))
//                .collect(Collectors.toList());
//        return new ResponseEntity<>(commentResponseDtos, HttpStatus.OK);
//    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity deleteComment(@PathVariable("commentId") Long commentId) {
        commentService.deleteComment(commentId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}