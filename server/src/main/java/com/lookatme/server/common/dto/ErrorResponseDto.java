package com.lookatme.server.common.dto;

import com.lookatme.server.exception.ErrorCode;
import lombok.Getter;
import org.springframework.validation.BindingResult;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class ErrorResponseDto {
    private String errorCode;
    private String message;
    private List<FieldError> fieldErrors;
    private List<ConstraintViolationError> violationErrors;

    private ErrorResponseDto() {

    }

    public ErrorResponseDto(final ErrorCode errorCode) {
        this.errorCode = errorCode.name();
        this.message = errorCode.getValue();
    }

    public ErrorResponseDto(final BindingResult bindingResult) {
        this.errorCode = "BAD_REQUEST";
        this.message = "에러 발생";
        this.fieldErrors = FieldError.of(bindingResult);
    }

    public ErrorResponseDto(final Set<ConstraintViolation<?>> violations) {
        this.errorCode = "BAD_REQUEST";
        this.message = "에러 발생";
        this.violationErrors = ConstraintViolationError.of(violations);
    }

    @Getter
    public static class FieldError {
        private final String field;
        private final Object rejectedValue;
        private final String reason;

        private FieldError(String field, Object rejectedValue, String reason) {
            this.field = field;
            this.rejectedValue = rejectedValue;
            this.reason = reason;
        }

        public static List<FieldError> of(BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors
                    = bindingResult.getFieldErrors();

            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()
                    )).collect(Collectors.toList());
        }
    }

    @Getter
    public static class ConstraintViolationError {
        private final Object rejectedValue;
        private final String reason;

        private ConstraintViolationError(Object rejectedValue, String reason) {
            this.rejectedValue = rejectedValue;
            this.reason = reason;
        }

        public static List<ConstraintViolationError> of(
                Set<ConstraintViolation<?>> constraintViolations) {
            return constraintViolations.stream()
                    .map(constraintViolation -> new ConstraintViolationError(
                            constraintViolation.getInvalidValue().toString(),
                            constraintViolation.getMessage()
                    )).collect(Collectors.toList());
        }
    }
}
