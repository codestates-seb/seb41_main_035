package com.lookatme.server.auth.utils;

import com.google.gson.Gson;
import com.lookatme.server.common.dto.ErrorResponseDto;
import com.lookatme.server.exception.ErrorCode;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ErrorResponder {

    public static void sendErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        Gson gson = new Gson();
        ErrorResponseDto errorResponse = new ErrorResponseDto(errorCode);
        response.setCharacterEncoding("utf-8"); // * 필수 * (없으면 한글이 ?? 으로 보임)
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(errorCode.getStatus().value());
        response.getWriter().write(gson.toJson(errorResponse, ErrorResponseDto.class));
    }
}
