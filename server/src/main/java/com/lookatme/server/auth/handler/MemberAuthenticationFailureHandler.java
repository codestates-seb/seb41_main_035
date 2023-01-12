package com.lookatme.server.auth.handler;

import com.lookatme.server.auth.utils.ErrorResponder;
import com.lookatme.server.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j // 로그인 인증 실패 시 진행
public class MemberAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        log.error(">> 로그인 실패: {}", exception.getMessage());
        ErrorResponder.sendErrorResponse(response, ErrorCode.LOGIN_FAILED);
    }
}
