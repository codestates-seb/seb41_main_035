package com.lookatme.server.auth.handler;

import com.lookatme.server.auth.utils.ErrorResponder;
import com.lookatme.server.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component // 토큰 검증에 실패했을 경우 처리
public class MemberAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ErrorCode errorCode = (ErrorCode) request.getAttribute("exception");
        if(errorCode == null) {
            errorCode = ErrorCode.AUTHENTICATION_FAILED;
        }
        ErrorResponder.sendErrorResponse(response, errorCode);
        log.error(">> Unauthorized Error 발생: {}", errorCode.getValue());
    }
}
