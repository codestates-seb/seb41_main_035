package com.lookatme.server.exception;

import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // COMMENT
    COMMENT_NOT_FOUND("해당 댓글은 존재하지 않습니다.", HttpStatus.NOT_FOUND),

    // MEMBER
    MEMBER_NOT_FOUND("회원이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    MEMBER_EXISTS("이미 존재하는 회원입니다.", HttpStatus.BAD_REQUEST),
    MEMBER_NICKNAME_EXISTS("이미 존재하는 닉네임입니다.", HttpStatus.BAD_REQUEST),

    // Security & JWT
    TOKEN_LOGOUT("로그아웃된 토큰입니다.", HttpStatus.FORBIDDEN), // Forbidden = 사용자가 누구인지 알고있으나 권한이 없음
    TOKEN_INVALID("사용할 수 없는 토큰입니다.", HttpStatus.UNAUTHORIZED), // Unauthorized = 비인증 상태
    TOKEN_EXPIRE("만료된 토큰입니다.", HttpStatus.FORBIDDEN),
    UNAUTHORIZED("접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    EXCEPTION("알 수 없는 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String value;
    private final HttpStatus status;

    ErrorCode(String value, HttpStatus status) {
        this.value = value;
        this.status = status;
    }

    @JsonValue
    public String getValue(){
        return value;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
