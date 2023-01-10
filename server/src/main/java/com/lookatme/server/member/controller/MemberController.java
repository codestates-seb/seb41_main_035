package com.lookatme.server.member.controller;

import com.lookatme.server.auth.dto.MemberPrincipal;
import com.lookatme.server.common.dto.MultiResponseDto;
import com.lookatme.server.member.dto.MemberDto;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.mapper.MemberMapper;
import com.lookatme.server.auth.service.AuthService;
import com.lookatme.server.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RequiredArgsConstructor
@Validated
@RequestMapping("/members")
@RestController
public class MemberController {

    private final AuthService authService;
    private final MemberService memberService;
    private final MemberMapper mapper;

    // 회원 단일 조회
    @GetMapping("/{memberId}")
    public ResponseEntity<?> getMember(@Positive @PathVariable long memberId) {
        return new ResponseEntity<>(
                mapper.memberToMemberResponse(memberService.findMember(memberId)),
                HttpStatus.OK);
    }

    // 회원 목록 조회
    @GetMapping
    public ResponseEntity<?> getMembers(@Positive @RequestParam(defaultValue = "1") int page,
                                        @Positive @RequestParam(defaultValue = "10") int size) {
        Page<Member> pageMembers = memberService.findMembers(page - 1, size);
        return new ResponseEntity<>(
                new MultiResponseDto<>(
                        mapper.memberListToMemberResponseList(pageMembers.getContent()),
                        pageMembers),
                HttpStatus.OK
        );
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity<?> updateMember(@Positive @PathVariable long memberId,
                                          @Valid @RequestBody MemberDto.Patch patchDto) {
        Member member = mapper.memberPatchDtoToMember(patchDto, memberId);
        return new ResponseEntity<>(
                mapper.memberToMemberResponse(memberService.updateMember(member)),
                HttpStatus.OK);
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<?> deleteMember(@Positive @PathVariable long memberId) {
        memberService.deleteMember(memberId);
        return new ResponseEntity<>("회원탈퇴 되었습니다", HttpStatus.NO_CONTENT);
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

    @GetMapping("/jwt-test") // Access 토큰 유효성 테스트용
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

        response.setHeader("Authorization", "Bearer " + newAccessToken);
        return new ResponseEntity<>(newAccessToken, HttpStatus.CREATED);
    }
}
