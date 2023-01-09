package com.lookatme.server.exception;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ErrorCode {
    //COMMENT
    COMMENT_NOT_FOUND("COMMENT_NOT_FOUND");

    private final String value;

    ErrorCode(final String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue(){
        return value;
    }
}
