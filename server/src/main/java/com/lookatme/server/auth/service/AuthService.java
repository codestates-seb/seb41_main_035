package com.lookatme.server.auth.service;

import com.lookatme.server.auth.jwt.JwtTokenizer;
import com.lookatme.server.auth.jwt.RedisRepository;
import com.lookatme.server.exception.ErrorCode;
import com.lookatme.server.exception.ErrorLogicException;
import com.lookatme.server.member.entity.Account;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final RedisRepository redisRepository;
    private final MemberRepository memberRepository;
    private final JwtTokenizer jwtTokenizer;

    public void logout(String accessToken, String memberUniqueKey) {
        redisRepository.expireRefreshToken(memberUniqueKey); // 1. redis에 저장된 Refresh 토큰 삭제 (액세스 토큰 재발급 방지)
        redisRepository.addAccessTokenToBlacklist(accessToken); // 2. 액세스 토큰을 블랙리스트에 추가해 해당 토큰 사용 못하도록 차단
    }

    // 단순 전달용 메서드
    public void addAccessTokenToBlacklist(String accessToken) {
        if(accessToken.startsWith("Bearer")) {
            accessToken = accessToken.replace("Bearer ", "");
        }
        redisRepository.addAccessTokenToBlacklist(accessToken);
    }

    public String getTokenSubject(String token) {
        return jwtTokenizer.getTokenSubject(token);
    }

    public String reissueAccessToken(String refreshToken, String tokenSubject) {
        // 1. Redis에 저장되지 않은 (사용할 수 없는) RTK인 경우 예외 발생
        if (!redisRepository.hasRefreshToken(refreshToken, tokenSubject)) {
            throw new ErrorLogicException(ErrorCode.TOKEN_INVALID);
        }

        // 2. tokenSubject를 통해 회원 조회
        Account account = Member.uniqueKeyToAccount(tokenSubject);
        Member member = memberRepository.findByAccount(account)
                .orElseThrow(() -> new ErrorLogicException(ErrorCode.MEMBER_NOT_FOUND));

        // 3. ATK 재발급
        return jwtTokenizer.delegateAccessToken(member);
    }
}
