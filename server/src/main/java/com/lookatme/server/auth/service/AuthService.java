package com.lookatme.server.auth.service;

import com.lookatme.server.auth.jwt.JwtTokenizer;
import com.lookatme.server.auth.jwt.RedisRepository;
import com.lookatme.server.exception.ErrorCode;
import com.lookatme.server.exception.ErrorLogicException;
import com.lookatme.server.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final RedisRepository redisRepository;
    private final JwtTokenizer jwtTokenizer;

    public void logout(String accessToken, String memberUniqueKey) {
        redisRepository.expireRefreshToken(memberUniqueKey); // 1. redis에 저장된 Refresh 토큰 삭제 (액세스 토큰 재발급 방지)
        redisRepository.addAccessTokenToBlacklist(accessToken); // 2. 액세스 토큰을 블랙리스트에 추가해 해당 토큰 사용 못하도록 차단
    }

    public String getMemberUniqueKeyAtToken(String token) {
        return jwtTokenizer.getTokenSubject(token);
    }

    public String reissueAccessToken(String refreshToken, Member member) {
        // 1. 유효한 RTK인지? (redis에 저장되어 있는지?)
        String tokenSubject = jwtTokenizer.getTokenSubject(refreshToken);

        if (!redisRepository.hasRefreshToken(refreshToken, tokenSubject)) {
            throw new ErrorLogicException(ErrorCode.TOKEN_INVALID);
        }

        // 2. ATK 재발급
        Map<String, Object> claims = jwtTokenizer.generateClaims(member);

        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        String accessToken = jwtTokenizer.generateAccessToken(claims, tokenSubject, expiration, base64EncodedSecretKey);

        return accessToken;
    }
}
