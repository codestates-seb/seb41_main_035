package com.lookatme.server;

import com.lookatme.server.exception.ErrorLogicException;
import com.lookatme.server.exception.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {
    private static final String ERROR_MESSAGE = "[예외 발생] : {}";

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponseDto handleBadRequestException(final ErrorLogicException e) {
        return new ErrorResponseDto(e.getErrorCode(), e.getMessage());
    }
}
