package com.lookatme.server.exception.message;

import com.lookatme.server.exception.ErrorCode;
import com.lookatme.server.exception.ErrorLogicException;

public class MessageNotFoundException extends ErrorLogicException {
    public MessageNotFoundException() {
        super(ErrorCode.MESSAGE_NOT_FOUND, ErrorCode.MESSAGE_NOT_FOUND.getValue());
    }
}
