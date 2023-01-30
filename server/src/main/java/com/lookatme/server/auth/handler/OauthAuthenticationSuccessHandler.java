package com.lookatme.server.auth.handler;

import com.lookatme.server.auth.jwt.JwtTokenizer;
import com.lookatme.server.member.entity.Account;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.entity.OauthPlatform;
import com.lookatme.server.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class OauthAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenizer jwtTokenizer;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        Member member = getOrRegisterMember(oAuth2User.getAttributes());
        String accessToken = delegateAccessToken(member);
        String refreshToken = delegateRefreshToken(member);

        String uri = createURI(accessToken, refreshToken).toString();
        getRedirectStrategy().sendRedirect(request, response, uri);
    }

    private Member getOrRegisterMember(Map<String, Object> attributes) {
        String email = attributes.get("email").toString();
        String nickname = checkValidNickname(attributes.get("name").toString());
        String profileImgLink = attributes.get("picture").toString();

        Member savedMember;
        Account account = new Account(email, OauthPlatform.GOOGLE);
        savedMember = memberRepository.findByAccount(account)
                .orElseGet(() -> {
                    Member member = Member.builder()
                            .account(account)
                            .nickname(nickname)
                            .profileImageUrl(profileImgLink).build();

                    log.info(">> 소셜 회원가입 진행: {}", nickname);
                    return memberRepository.save(member);
                });

        return savedMember;
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

    // 최소한의 닉네임 중복 방지
    private String checkValidNickname(String nickname) {
        int idx = 1;
        while (memberRepository.existsByNickname(nickname)) {
            nickname = String.format("%s-%d", nickname, idx++);
        }
        return nickname;
    }

    private URI createURI(String accessToken, String refreshToken) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("access_token", accessToken);
        queryParams.add("refresh_token", refreshToken);

        return UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .host("mainproject-035.s3-website.ap-northeast-2.amazonaws.com")
                .path("/google")
                .queryParams(queryParams)
                .build()
                .toUri();
    }
}
