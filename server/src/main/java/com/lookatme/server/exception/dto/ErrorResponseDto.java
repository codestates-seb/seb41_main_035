package com.lookatme.server.exception.dto;

import com.lookatme.server.exception.ErrorCode;
import lombok.Getter;

@Getter
public class ErrorResponseDto {
    private ErrorCode errorCode;
    private String message;

    private ErrorResponseDto() {

    }

    public ErrorResponseDto(final ErrorCode errorCode, final String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
}
