package com.lookatme.server.auth.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Repository
public class RedisRepository {

    private final JwtTokenizer jwtTokenizer;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisTemplate<String, Object> redisBlacklistTemplate;

    /**
     * 로그인에 성공하면 RefreshToken을 Redis에 저장
     * - refresh 토큰의 만료기간 동안만 저장함
     */
    public void saveRefreshToken(String refreshToken, String memberUniqueKey) {
        ValueOperations<String, Object> map = redisTemplate.opsForValue();
        map.set(memberUniqueKey,
                refreshToken,
                jwtTokenizer.getRefreshTokenExpirationMinutes(), TimeUnit.MINUTES);
    }

    /**
     * 로그아웃 후 AccessToken을 블랙리스트에 넣어 해당 토큰으로 로그인 할 수 없도록 차단
     * ATK의 원래 만료 시간 - 로그아웃 시간 = 남은 시간 만큼 동안만 블랙리스트에 저장
     */
    public void addAccessTokenToBlacklist(String accessToken) {
        ValueOperations<String, Object> map = redisBlacklistTemplate.opsForValue();
        long remainDatetime = jwtTokenizer.calculateRemainExpiration(accessToken);
        map.set(accessToken, "ATK", remainDatetime, TimeUnit.MILLISECONDS);
    }

    // 로그아웃 하게되면 RefreshToken은 더이상 저장해둘 필요 없으므로 삭제
    public void expireRefreshToken(String memberUniqueKey) {
        redisTemplate.delete(memberUniqueKey);
    }

    // 블랙리스트 안에 AccessToken이 있는지?
    public boolean hasAccessTokenInBlacklist(String accessToken) {
        return Boolean.TRUE.equals(redisBlacklistTemplate.hasKey(accessToken));
    }

    public boolean hasRefreshToken(String memberUniqueKey, String refreshToken) {
        // 해당 회원에 대한 키값이 존재하는 경우에만
        if(Boolean.TRUE.equals(redisTemplate.hasKey(memberUniqueKey))) {
            ValueOperations<String, Object> map = redisTemplate.opsForValue();
            // 전달받은 RTK 토큰과 저장된 토큰값이 같은지 비교
            String savedRefreshToken = map.get(memberUniqueKey).toString();
            return savedRefreshToken.equals(refreshToken);
        } else {
            return false;
        }
    }
}
