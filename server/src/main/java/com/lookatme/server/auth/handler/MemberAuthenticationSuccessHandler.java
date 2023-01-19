package com.lookatme.server.auth.handler;

import com.google.gson.Gson;
import com.lookatme.server.auth.dto.LoginResponse;
import com.lookatme.server.auth.userdetails.MemberDetails;
import com.lookatme.server.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
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

        // 로그인 유저 정보 반환
        Gson gson = new Gson();
        Member member = memberDetails.getMember();
        LoginResponse loginMemberResponse = new LoginResponse(member);

        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(gson.toJson(loginMemberResponse));
    }

}
