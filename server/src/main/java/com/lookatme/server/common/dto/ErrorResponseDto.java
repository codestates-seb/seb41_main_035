package com.lookatme.server.common.dto;

import com.lookatme.server.exception.ErrorCode;
import lombok.Getter;

@Getter
public class ErrorResponseDto {
    private String errorCode;
    private String message;

    private ErrorResponseDto() {

    }

    public ErrorResponseDto(final ErrorCode errorCode) {
        this.errorCode = errorCode.name();
        this.message = errorCode.getValue();
    }
}
