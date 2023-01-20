package com.lookatme.server.exception.board;

import com.lookatme.server.exception.ErrorCode;
import com.lookatme.server.exception.ErrorLogicException;

public class BoardNotFoundException extends ErrorLogicException {
    public BoardNotFoundException() {
        super(ErrorCode.BOARD_NOT_FOUND, ErrorCode.BOARD_NOT_FOUND.getValue());
    }
}
