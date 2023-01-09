package com.lookatme.server;

import com.lookatme.server.exception.ErrorCode;
import com.lookatme.server.exception.ErrorLogicException;
import com.lookatme.server.common.dto.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {
    private static final String ERROR_MESSAGE = "[예외 발생] : {}";

    @ExceptionHandler
    public ResponseEntity<?> handleBadRequestException(final ErrorLogicException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.error(ERROR_MESSAGE, errorCode.getValue());
        return new ResponseEntity<>(new ErrorResponseDto(errorCode), errorCode.getStatus());
    }
}
