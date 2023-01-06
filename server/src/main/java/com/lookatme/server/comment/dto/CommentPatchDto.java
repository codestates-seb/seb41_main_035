package com.lookatme.server.comment.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class CommentPatchDto {
    @NotBlank
    private String content;

    private CommentPatchDto() {

    }

    @Builder
    public CommentPatchDto(final String content) {
        this.content = content;
    }
}
