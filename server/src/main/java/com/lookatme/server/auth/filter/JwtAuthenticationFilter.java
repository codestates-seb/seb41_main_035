package com.lookatme.server.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lookatme.server.auth.dto.LoginDto;
import com.lookatme.server.auth.jwt.JwtTokenizer;
import com.lookatme.server.auth.jwt.RedisRepository;
import com.lookatme.server.auth.userdetails.MemberDetails;
import com.lookatme.server.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final RedisRepository redisRepository;
    private final JwtTokenizer jwtTokenizer;

    @SneakyThrows
    @Override // LoginDto를 받아서 로그인 인증 -> MemberDetailsService의 loadUserByUsername을 이용
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        ObjectMapper objectMapper = new ObjectMapper();
        LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(), loginDto.getPassword());
        request.setAttribute("email", loginDto.getEmail());

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override // 로그인 인증에 성공했을 경우 JWT 토큰 만들어서 Success 핸들러로 전달
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws ServletException, IOException {
        MemberDetails memberDetails = (MemberDetails) authResult.getPrincipal();
        Member member = memberDetails.getMember();

        String accessToken = delegateAccessToken(member);
        String refreshToken = delegateRefreshToken(member);

        // JWT 토큰을 만들어서 Response 헤더로 전달
        response.setHeader("Authorization", accessToken);
        response.setHeader("Refresh", refreshToken);

        // Redis 저장소에 RefreshToken을 저장
        redisRepository.saveRefreshToken(refreshToken, member.getUniqueKey());

        this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
    }

    // Access token 생성 ** 꼭 필요한 정보만 담는 것이 좋음 **
    private String delegateAccessToken(Member member) {
        String accessToken = jwtTokenizer.delegateAccessToken(member);
        return accessToken;
    }

    private String delegateRefreshToken(Member member) {
        String subject = member.getUniqueKey();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        String refreshToken = jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey);

        return refreshToken;
    }
}
