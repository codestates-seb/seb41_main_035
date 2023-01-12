package com.lookatme.server.auth.jwt;

import com.lookatme.server.member.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.io.Decoders;
import org.junit.jupiter.api.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JwtTokenizerTest {

    private static JwtTokenizer jwtTokenizer = new JwtTokenizer();
    private String secretKey;
    private String base64EncodedSecretKey;

    @BeforeEach
    public void init() {
        // 단위 테스트에서 @Value 애노테이션을 대체하기 위해서는 Reflection을 이용해 값을 주입해줘야 한다
        ReflectionTestUtils.setField(jwtTokenizer, "secretKey", "qwe!@#QEWF#HQN$Q%MY$M3567j5j%^J#%^J#%^J");
        ReflectionTestUtils.setField(jwtTokenizer, "accessTokenExpirationMinutes", 30);
        ReflectionTestUtils.setField(jwtTokenizer, "refreshTokenExpirationMinutes", 360);

        secretKey = jwtTokenizer.getSecretKey();
        base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(secretKey);
    }

    @DisplayName("비밀키가 Base64형식으로 정상적으로 인코딩 되는지 테스트")
    @Test
    public void encodeBase64SecretKeyTest() {
        assertThat(secretKey).isEqualTo(new String(Decoders.BASE64.decode(base64EncodedSecretKey)));
    }

    @DisplayName("회원을 통해 Claims를 잘 생성하는지 테스트")
    @Test
    public void generateClaimsTest() {
        Member member = getAuthenticatedMember();
        Map<String, Object> claims = jwtTokenizer.generateClaims(member);

        assertThat(claims.get("memberId")).isEqualTo(member.getMemberId());
        assertThat(claims.get("email")).isEqualTo(member.getEmail());
        assertThat(claims.get("oauthPlatform")).isEqualTo(member.getOauthPlatform());
        assertThat(claims.get("memberUniqueKey")).isEqualTo(member.getUniqueKey());
        assertThat(claims.get("roles")).isEqualTo(member.getRoles());
    }

    @DisplayName("액세스 토큰이 제대로 생성되는지 테스트")
    @Test
    public void generateAccessTokenTest() {
        // Member 기반으로 Access Token 생성
        Member member = getAuthenticatedMember();
        String accessToken = jwtTokenizer.delegateAccessToken(member);
        System.out.println("생성된 액세스 토큰 >> " + accessToken);
        assertThat(accessToken).startsWith("Bearer ");

        // Access Token을 복호화 후 claims로 변환
        String jws = accessToken.replace("Bearer ", "");
        Claims claims = jwtTokenizer.getClaims(jws, base64EncodedSecretKey).getBody();

        assertThat(claims.getSubject()).isEqualTo(member.getUniqueKey());
        assertThat(Long.parseLong(claims.get("memberId").toString())).isEqualTo(member.getMemberId());
        assertThat(claims.get("email")).isEqualTo(member.getEmail());
        assertThat(claims.get("oauthPlatform")).isEqualTo(member.getOauthPlatform().name());
        assertThat(claims.get("memberUniqueKey")).isEqualTo(member.getUniqueKey());
        assertThat(claims.get("roles")).isEqualTo(member.getRoles());
    }

    @DisplayName("토큰 만료 시간이 지나면 예외가 터지는지 테스트")
    @Test
    public void verifySignatureTest() throws InterruptedException {
        Member member = getAuthenticatedMember();
        Map<String, Object> claims = jwtTokenizer.generateClaims(member);

        String accessToken = getAccessToken(claims, Calendar.SECOND, 2);

        assertDoesNotThrow(() -> jwtTokenizer.getClaims(accessToken, base64EncodedSecretKey));

        System.out.println(">> 2.5초 대기...");
        TimeUnit.MILLISECONDS.sleep(2500);

        assertThrows(ExpiredJwtException.class, () -> jwtTokenizer.getClaims(accessToken, base64EncodedSecretKey));
    }

    private String getAccessToken(Map<String, Object> claims, int timeUnit, int timeAmount) {
        String subject = claims.get("memberUniqueKey").toString();
        Calendar calendar = Calendar.getInstance();
        calendar.add(timeUnit, timeAmount);
        Date expiration = calendar.getTime();

        return jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);
    }

    private Member getAuthenticatedMember() {
        return Member.builder()
                .memberId(1L)
                .email("email@com")
                .oauthPlatform(Member.OauthPlatform.GOOGLE).build();
    }
}