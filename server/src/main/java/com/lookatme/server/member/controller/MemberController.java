package com.lookatme.server.member.controller;

import com.lookatme.server.auth.dto.MemberPrincipal;
import com.lookatme.server.member.dto.MemberDto;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.mapper.MemberMapper;
import com.lookatme.server.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

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
}
