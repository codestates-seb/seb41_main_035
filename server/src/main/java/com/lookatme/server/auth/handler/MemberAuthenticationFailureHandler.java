package com.lookatme.server.auth.handler;

import com.lookatme.server.auth.utils.ErrorResponder;
import com.lookatme.server.exception.ErrorCode;
import com.lookatme.server.exception.ErrorLogicException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j // 로그인 인증 실패 시 진행
@RequiredArgsConstructor
public class MemberAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final LoginTransactionalListener loginTransactionalListener;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        log.error(">> 로그인 실패: {}", exception.getMessage());

        ErrorCode errorCode = ErrorCode.EXCEPTION;
        if (exception.getCause() instanceof ErrorLogicException) {
            errorCode = ErrorCode.LOGIN_ACCOUNT_FAILED; // 계정이 없는 경우
        } else if (exception instanceof LockedException) {
            errorCode = ErrorCode.LOGIN_ACCOUNT_LOCKED; // 계정이 잠긴 경우 (로그인 5회 이상 실패)
        } else if (exception instanceof BadCredentialsException) {
            String email = (String) request.getAttribute("email"); // 로그인 실패 (5회 미만)
            int loginFailedCnt = loginTransactionalListener.loginFailed(email);
            String errorCodeStr = ErrorCode.LOGIN_PASSWORD_FAILED.name();
            String message = ErrorCode.LOGIN_PASSWORD_FAILED.getValue() + String.format(" (%d/5)", loginFailedCnt);
            ErrorResponder.sendErrorResponse(response, errorCodeStr, message); // 별도의 errorResponder를 이용
            return;
        }
        ErrorResponder.sendErrorResponse(response, errorCode);
    }
}

