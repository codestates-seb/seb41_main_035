package com.lookatme.server.auth.handler;

import com.lookatme.server.auth.utils.ErrorResponder;
import com.lookatme.server.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component // 인증에는 성공했으나 권한이 없는 경우
public class MemberAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ErrorResponder.sendErrorResponse(response, ErrorCode.UNAUTHORIZED);
        log.error(">> Forbidden error 발생: {}", accessDeniedException.getMessage());
    }
}
