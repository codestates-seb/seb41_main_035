package com.lookatme.server.exception;

import lombok.Getter;

@Getter
public class ErrorLogicException extends RuntimeException {
    private final ErrorCode errorCode;

    public ErrorLogicException(final ErrorCode errorCode) {
        super(errorCode.getValue());
        this.errorCode = errorCode;
    }

    public ErrorLogicException(final ErrorCode errorCode, final String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
