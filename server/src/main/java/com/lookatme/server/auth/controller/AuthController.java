package com.lookatme.server.auth.controller;

import com.lookatme.server.auth.dto.MemberPrincipal;
import com.lookatme.server.auth.service.AuthService;
import com.lookatme.server.member.dto.MemberDto;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.mapper.MemberMapper;
import com.lookatme.server.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final MemberService memberService;
    private final AuthService authService;
    private final MemberMapper mapper;
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

    @PostMapping("/signup")
    public ResponseEntity<?> registerMember(@Valid @RequestBody MemberDto.Post postDto) {
        // 1. DTO -> Member 변환
        Member member = mapper.memberPostDtoToMember(postDto);
        // 2. 비밀번호 암호화
        authService.encodePassword(member);
        // 3. 회원 등록
        return new ResponseEntity<>(
                mapper.memberToMemberResponse(memberService.registerMember(member)),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal MemberPrincipal memberPrincipal,
                                    @RequestHeader("Authorization") String authHeader) {
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

        // 1. Refresh 토큰에서 회원 식별값 꺼내옴 -> 회원 조회
        String memberUniqueKey = authService.getMemberUniqueKeyAtToken(refreshToken);
        Member member = memberService.findMember(memberUniqueKey);

        // 2. DB에서 찾아온 회원 정보를 통해 Access 토큰 재발급
        String newAccessToken = authService.reissueAccessToken(refreshToken, member);
        authService.addAccessTokenToBlacklist(accessToken); // 기존에 사용하던 액세스 토큰은 사용할 수 없도록 블랙리스트 등록

        response.setHeader("Authorization", newAccessToken);
        return new ResponseEntity<>(newAccessToken, HttpStatus.CREATED);
    }
}
