package com.lookatme.server.member.controller;

import com.lookatme.server.common.dto.MultiResponseDto;
import com.lookatme.server.member.dto.MemberDto;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.mapper.MemberMapper;
import com.lookatme.server.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RequiredArgsConstructor
@Validated
@RequestMapping("/members")
@RestController
public class MemberController {

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
}
