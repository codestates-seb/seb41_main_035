package com.lookatme.server.exception.message;

import com.lookatme.server.exception.ErrorCode;
import com.lookatme.server.exception.ErrorLogicException;

public class MessageNotSendToSelfException extends ErrorLogicException {
    public MessageNotSendToSelfException() {
        super(ErrorCode.MESSAGE_NOT_SEND_TO_SELF, ErrorCode.MESSAGE_NOT_SEND_TO_SELF.getValue());
    }
}
