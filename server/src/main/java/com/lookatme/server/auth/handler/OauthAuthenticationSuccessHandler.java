package com.lookatme.server.auth.handler;

import com.lookatme.server.auth.jwt.JwtTokenizer;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.service.MemberService;
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
    private final MemberService memberService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        Member member = saveMember(oAuth2User.getAttributes());
        String accessToken = delegateAccessToken(member);
        String refreshToken = delegateRefreshToken(member);

        String uri = createURI(accessToken, refreshToken).toString();
        getRedirectStrategy().sendRedirect(request, response, uri);
    }

    private Member saveMember(Map<String, Object> attributes) {
        String email = attributes.get("email").toString();
        String nickname = checkValidNickname(attributes.get("name").toString());
        String profileImgLink = attributes.get("picture").toString();

        Member member = Member.builder()
                .email(email)
                .nickname(nickname)
                .profileImageUrl(profileImgLink)
                .oauthPlatform(Member.OauthPlatform.GOOGLE).build();

        Member savedMember = memberService.registerMember(member);
        log.info(">> 소셜 회원가입 완료: {}", savedMember.getUniqueKey());
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
        while(memberService.hasNickname(nickname)) {
            nickname = String.format("%s-%d", nickname, idx++);
        }
        return nickname;
    }

    // TODO: 추후 프론트엔드랑 맞춰야함
    private URI createURI(String accessToken, String refreshToken) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("access_token", accessToken);
        queryParams.add("refresh_token", refreshToken);

        return UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .host("localhost")
                .port(8080)
                .path("/")
                .queryParams(queryParams)
                .build()
                .toUri();
    }
}
