package com.lookatme.server.exception;

import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.http.protocol.HTTP;
import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // COMMENT
    COMMENT_NOT_FOUND("해당 댓글은 존재하지 않습니다.", HttpStatus.NOT_FOUND),

    // MESSAGE
    MESSAGE_NOT_FOUND("해당 메시지는 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    MESSAGE_NOT_SEND_TO_SELF("자기 자신에게는 메시지를 보낼 수 없습니다.", HttpStatus.BAD_REQUEST),

    // BOARD
    BOARD_NOT_FOUND("해당 게시글은 존재하지 않습니다.", HttpStatus.NOT_FOUND),

    // PRODUCT & Rental
    PRODUCT_NOT_FOUND("상품이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    PRODUCT_ALREADY_EXISTS("상품이 이미 존재합니다.", HttpStatus.BAD_REQUEST),
    RENTAL_NOT_FOUND("렌탈 정보가 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    RENTAL_ALREADY_EXISTS("렌탈 정보가 이미 존재합니다.", HttpStatus.BAD_REQUEST),

    // MEMBER
    MEMBER_NOT_FOUND("회원이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    MEMBER_ACCOUNT_EXISTS("이미 존재하는 계정입니다.", HttpStatus.BAD_REQUEST),
    MEMBER_NICKNAME_EXISTS("이미 존재하는 닉네임입니다.", HttpStatus.BAD_REQUEST),
    MEMBER_ALREADY_FOLLOW("이미 팔로우 했습니다", HttpStatus.BAD_REQUEST),
    MEMBER_SELF_FOLLOW("자기 자신을 팔로우 할 수 없습니다", HttpStatus.BAD_REQUEST),
    MEMBER_NOT_FOLLOW("팔로우 한 회원이 아닙니다", HttpStatus.BAD_REQUEST),

    // Category
    CATEGORY_NOT_FOUND("카테고리가 존재하지 않습니다.", HttpStatus.NOT_FOUND),


    // Security & JWT
    TOKEN_LOGOUT("로그아웃된 토큰입니다.", HttpStatus.FORBIDDEN), // Forbidden = 사용자가 누구인지 알고있으나 권한이 없음
    TOKEN_INVALID("사용할 수 없는 토큰입니다.", HttpStatus.UNAUTHORIZED), // Unauthorized = 비인증 상태
    TOKEN_EXPIRE("만료된 토큰입니다.", HttpStatus.FORBIDDEN),
    AUTHENTICATION_FAILED("검증에 실패했습니다.(액세스 토큰을 보냈는지 확인해주세요)", HttpStatus.FORBIDDEN),
    LOGIN_ACCOUNT_FAILED("계정이 존재하지 않습니다", HttpStatus.BAD_REQUEST),
    LOGIN_PASSWORD_FAILED("비밀번호가 틀렸습니다", HttpStatus.BAD_REQUEST),
    LOGIN_ACCOUNT_LOCKED("계정이 잠겼습니다", HttpStatus.BAD_REQUEST),

    // File
    FILE_SAVE_FAILED("파일 저장에 실패했습니다.", HttpStatus.BAD_REQUEST),
    FILE_CONVERT_FAILED("파일 변환에 실패했습니다", HttpStatus.BAD_REQUEST),
    FILE_TYPE_NOT_SUPPORTED("지원하지 않는 파일 타입입니다", HttpStatus.BAD_REQUEST),
    FILE_SIZE_EXCEEDED("파일 용량을 초과했습니다", HttpStatus.BAD_REQUEST),

    // 공통
    UNAUTHORIZED("로그인 후 시도해주세요.", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    EXCEPTION("알 수 없는 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);

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
