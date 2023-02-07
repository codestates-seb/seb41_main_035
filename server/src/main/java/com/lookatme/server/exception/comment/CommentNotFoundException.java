package com.lookatme.server.exception.comment;

import com.lookatme.server.exception.ErrorCode;
import com.lookatme.server.exception.ErrorLogicException;

public class CommentNotFoundException extends ErrorLogicException {
    public CommentNotFoundException() {
        super(ErrorCode.COMMENT_NOT_FOUND, ErrorCode.COMMENT_NOT_FOUND.getValue());
    }
}
