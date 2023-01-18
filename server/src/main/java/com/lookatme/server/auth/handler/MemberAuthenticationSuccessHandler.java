package com.lookatme.server.auth.handler;

import com.lookatme.server.auth.userdetails.MemberDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class MemberAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final LoginTransactionalListener listener;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        MemberDetails memberDetails = (MemberDetails) authentication.getPrincipal();
        log.info(">> 로그인 성공: {}", memberDetails.getUsername());
        String email = (String) request.getAttribute("email");
        listener.loginSuccess(email);
    }
}
