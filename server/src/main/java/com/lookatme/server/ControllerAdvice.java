package com.lookatme.server;

import com.lookatme.server.exception.ErrorCode;
import com.lookatme.server.exception.ErrorLogicException;
import com.lookatme.server.common.dto.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

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

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(ERROR_MESSAGE, e.getTarget().getClass().getName() + " >> Binding Exception 발생");
        return new ErrorResponseDto(e.getBindingResult());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleConstraintViolationException(ConstraintViolationException e) {
        log.error(ERROR_MESSAGE, e.getMessage());
        return new ErrorResponseDto(e.getConstraintViolations());
    }
}
