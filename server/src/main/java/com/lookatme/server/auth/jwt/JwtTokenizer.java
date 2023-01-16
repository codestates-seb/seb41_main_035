package com.lookatme.server.auth.jwt;

import com.lookatme.server.member.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

@Getter
@Component
public class JwtTokenizer {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access-token-expiration-minutes}")
    private int accessTokenExpirationMinutes;

    @Value("${jwt.refresh-token-expiration-minutes}")
    private int refreshTokenExpirationMinutes;

    public String encodeBase64SecretKey(String secretKey) {
        return Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    // Member 정보를 가지고 Access Token 생성
    public String delegateAccessToken(Member member) {
        Map<String, Object> claims = generateClaims(member);
        String subject = member.getUniqueKey(); // subject = 사용자를 고유하게 구분할 수 있는 값으로 (이메일+소셜 플랫폼)
        Date expiration = getTokenExpiration(accessTokenExpirationMinutes);
        String base64EncodedSecretKey = encodeBase64SecretKey(secretKey);
        String accessToken = generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);

        return String.format("Bearer %s", accessToken);
    }

    // Access Token 생성
    public String generateAccessToken(Map<String, Object> claims,
                                      String subject,
                                      Date expiration,
                                      String base64EncodedSecretKey) {
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Refresh Token 생성
    public String generateRefreshToken(String subject, Date expiration, String base64EncodedSecretKey) {
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Member 엔티티를 통해 Claims 생성
    public Map<String, Object> generateClaims(Member member) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("memberId", member.getMemberId());
        claims.put("email", member.getEmail());
        claims.put("oauthPlatform", member.getOauthPlatform());
        claims.put("memberUniqueKey", member.getUniqueKey());
        claims.put("roles", member.getRoles());

        return claims;
    }

    public Jws<Claims> getClaims(String jws, String base64EncodedSecretKey) {
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jws);
    }

    // JWT 만료 일시를 지정하기 위한 메서드
    public Date getTokenExpiration(int expirationMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expirationMinutes);
        return calendar.getTime();
    }

    // 현재 토큰 남은 시간이 얼마나 되는지
    public long calculateRemainExpiration(String token) {
        Jws<Claims> claims = getClaims(token, encodeBase64SecretKey(secretKey));
        long expireDatetime = claims.getBody().getExpiration().getTime();
        long currentDatetime = Calendar.getInstance().getTimeInMillis();

        long remainDatetime = expireDatetime - currentDatetime;
        return remainDatetime < 0 ? 0 : remainDatetime;
    }

    private Key getKeyFromBase64EncodedKey(String base64EncodedSecretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getTokenSubject(String jws) {
        Jws<Claims> claims = getClaims(jws, encodeBase64SecretKey(secretKey));
        return claims.getBody().getSubject();
    }
}
