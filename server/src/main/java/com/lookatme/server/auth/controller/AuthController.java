package com.lookatme.server.auth.controller;

import com.lookatme.server.auth.dto.MemberPrincipal;
import com.lookatme.server.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final AuthService authService;
    private final Environment env;

    @GetMapping("/profile")
    public String profile() { // 현재 실행중인 real 프로필 조회 (8081인지 8082인지 확인하기 위함)
        List<String> profiles = Arrays.asList(env.getActiveProfiles()); // 현재 실행중인 ActiveProfile 조회 (dev, real1/2)
        List<String> realProfiles = Arrays.asList("real1", "real2");
        String defaultProfile = profiles.isEmpty() ? "dev" : profiles.get(0);

        return profiles.stream()
                .filter(realProfiles::contains)
                .findAny()
                .orElse(defaultProfile);
    }

    /**
     * 로그아웃
     * @param memberPrincipal -> 로그인 된 상태여야 함
     * @param authHeader -> 액세스 토큰을 블랙리스트에 등록해서 사용하지 못하도록 만들어야 함
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal MemberPrincipal memberPrincipal,
                                    @RequestHeader("Authorization") String authHeader) {
        // Redis 저장 시 액세스 토큰의 Bearer 떼고 저장
        String accessToken = authHeader.replace("Bearer ", "");
        authService.logout(accessToken, memberPrincipal.getMemberUniqueKey());
        return new ResponseEntity<>("로그아웃 되었습니다", HttpStatus.OK);
    }

    @PostMapping("/jwt-test") // Access 토큰 유효성 테스트용
    public String test(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        return memberPrincipal.toString();
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(
            @RequestHeader("Authorization") String accessToken,
            @RequestHeader("Refresh") String refreshToken,
            HttpServletResponse response) {

        // 1. Refresh 토큰에서 회원 식별값(Token Subject) 꺼내옴 -> 회원 조회
        String tokenSubject = authService.getTokenSubject(refreshToken);

        // 2.Access 토큰 재발급
        String newAccessToken = authService.reissueAccessToken(refreshToken, tokenSubject);
        authService.addAccessTokenToBlacklist(accessToken); // 기존에 사용하던 액세스 토큰은 사용할 수 없도록 블랙리스트 등록

        response.setHeader("Authorization", newAccessToken);
        return new ResponseEntity<>(newAccessToken, HttpStatus.CREATED);
    }
}
