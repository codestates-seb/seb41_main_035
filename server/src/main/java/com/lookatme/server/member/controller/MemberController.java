package com.lookatme.server.member.controller;

import com.lookatme.server.auth.dto.MemberPrincipal;
import com.lookatme.server.member.dto.MemberDto;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.mapper.MemberMapper;
import com.lookatme.server.auth.service.AuthService;
import com.lookatme.server.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RequiredArgsConstructor
@Validated
@RequestMapping("/members")
@RestController
public class MemberController { // TODO: Response 객체 통일하기

    private final AuthService authService;
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

    @GetMapping("/jwt-test") // Access 토큰 유효성 테스트용
    public String test(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        return memberPrincipal.toString();
    }

    @PostMapping("/signup")
    public MemberDto.Response registerMember(@Valid @RequestBody MemberDto.Post postDto) {
        Member member = mapper.memberPostDtoToMember(postDto);
        return mapper.memberToMemberResponse(memberService.registerMember(member));
    }

    @PostMapping("/logout")
    public String logout(@AuthenticationPrincipal MemberPrincipal memberPrincipal,
                         @RequestHeader("Authorization") String authHeader) {
        String accessToken = authHeader.replace("Bearer ", "");
        authService.logout(accessToken, memberPrincipal.getMemberUniqueKey());
        return "로그아웃";
    }

    @PostMapping("/reissue")
    public String reissue(@RequestHeader("Refresh") String refreshToken,
                          HttpServletResponse response) {

        // 1. Refresh 토큰에서 회원 식별값 꺼내옴 -> 회원 조회
        String memberUniqueKey = authService.getMemberUniqueKeyAtToken(refreshToken);
        String[] split = memberUniqueKey.split("/");
        Member member = memberService.findMember(split[0], Member.OauthPlatform.valueOf(split[1]));

        // 2. DB에서 찾아온 회원 정보를 통해 Access 토큰 재발급
        String accessToken = authService.reissueAccessToken(refreshToken, member);

        response.setHeader("Authorization", "Bearer " + accessToken);
        return accessToken;
    }
}
