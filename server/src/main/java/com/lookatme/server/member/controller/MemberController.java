package com.lookatme.server.member.controller;

import com.lookatme.server.auth.dto.MemberPrincipal;
import com.lookatme.server.auth.jwt.JwtTokenizer;
import com.lookatme.server.auth.jwt.RedisRepository;
import com.lookatme.server.member.dto.MemberDto;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.mapper.MemberMapper;
import com.lookatme.server.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Validated
@RequestMapping("/members")
@RestController
public class MemberController {

    private final MemberService memberService;
    private final MemberMapper mapper;

    @GetMapping("/{memberId}")
    public MemberDto.Response getMember(@Positive @PathVariable long memberId) {
        return mapper.memberToMemberResponse(memberService.findMember(memberId));
    }

    @GetMapping
    public List<MemberDto.Response> getMembers() {
        return mapper.memberListToMemberResponseList(memberService.findMembers());
    }

    @PostMapping("/signup")
    public MemberDto.Response registerMember(@Valid @RequestBody MemberDto.Post postDto) {
        Member member = mapper.memberPostDtoToMember(postDto);
        return mapper.memberToMemberResponse(memberService.registerMember(member));
    }

    @PostMapping("/logout")
    public String logout(@AuthenticationPrincipal MemberPrincipal memberPrincipal, HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization").replace("Bearer ", "");
        memberService.logout(accessToken, String.format("%s/%s", memberPrincipal.getEmail(), memberPrincipal.getOauthPlatform().name()));
        return "로그아웃";
    }

    @PatchMapping("/{memberId}")
    public MemberDto.Response updateMember(@Positive @PathVariable long memberId,
                                          @Valid @RequestBody MemberDto.Patch patchDto) {
        Member member = mapper.memberPatchDtoToMember(patchDto, memberId);
        return mapper.memberToMemberResponse(memberService.updateMember(member));
    }

    @DeleteMapping("/{memberId}")
    public String deleteMember(@Positive @PathVariable long memberId) {
        memberService.deleteMember(memberId);
        return "회원 탈퇴";
    }

    @GetMapping("/jwt-test")
    public String test(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        return memberPrincipal.toString();
    }

    // TODO: ******************** 리팩토링 필수 ********************
    private final RedisRepository redisRepository;
    private final JwtTokenizer jwtTokenizer;

    @PostMapping("/reissue")
    public String reissue(@RequestHeader("Refresh") String refreshToken,
                          HttpServletResponse response) {

        // 1. 유효한 RTK인지? (redis에 저장되어 있는지?)
        String tokenSubject = jwtTokenizer.getTokenSubject(refreshToken);

        if (!redisRepository.hasRefreshToken(refreshToken, tokenSubject)) {
            throw new AccessDeniedException("사용할 수 없는 Refresh 토큰입니다");
        }

        String[] split = tokenSubject.split("/");
        Member member = memberService.findMember(split[0], Member.OauthPlatform.valueOf(split[1]));

        // 2. ATK 재발급 --> 중복 요소
        Map<String, Object> claims = new HashMap<>();

        claims.put("memberId", member.getMemberId());
        claims.put("email", member.getEmail());
        claims.put("oauthPlatform", member.getOauthPlatform());
        claims.put("roles", member.getRoles());

        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        String accessToken = jwtTokenizer.generateAccessToken(claims, tokenSubject, expiration, base64EncodedSecretKey);

        response.setHeader("Authorization", "Bearer " + accessToken);
        return accessToken;
    }
}
