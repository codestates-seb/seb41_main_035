package com.lookatme.server.member.controller;

import com.lookatme.server.auth.dto.MemberPrincipal;
import com.lookatme.server.common.dto.MultiResponseDto;
import com.lookatme.server.exception.ErrorCode;
import com.lookatme.server.exception.ErrorLogicException;
import com.lookatme.server.file.service.FileService;
import com.lookatme.server.member.dto.MemberDto;
import com.lookatme.server.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.io.IOException;

@RequiredArgsConstructor
@Validated
@RequestMapping("/members")
@RestController
public class MemberController {

    private final MemberService memberService;
    private final FileService fileService;

    // 회원 단일 조회
    @GetMapping("/{memberId}")
    public ResponseEntity<?> getMember(@Positive @PathVariable long memberId) {
        return new ResponseEntity<>(memberService.findMember(memberId), HttpStatus.OK);
    }

    // 회원 목록 조회
    @GetMapping
    public ResponseEntity<?> getMembers(@Positive @RequestParam(defaultValue = "1") int page,
                                        @Positive @RequestParam(defaultValue = "10") int size,
                                        @AuthenticationPrincipal MemberPrincipal memberPrincipal,
                                        @RequestParam(required = false) String tab) {
        Page<MemberDto.Response> pageMembers;
        if (tab != null) {
            if (memberPrincipal == null) {
                throw new ErrorLogicException(ErrorCode.AUTHENTICATION_FAILED);
            }
            pageMembers = memberService.findFollowers(memberPrincipal.getAccount(), tab, page - 1, size);
        } else {
            pageMembers = memberService.findMembers(page - 1, size);
        }
        return new ResponseEntity<>(
                new MultiResponseDto<>(pageMembers.getContent(), pageMembers),
                HttpStatus.OK
        );
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerMember(@Valid @RequestBody MemberDto.Post postDto) {
        MemberDto.Response response = memberService.registerMember(postDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity<?> updateMember(@Positive @PathVariable long memberId,
                                          @Valid @RequestBody MemberDto.Patch patchDto,
                                          @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        // 로그인 유저와 정보가 다르다면 수정 불가 (접근 권한 X)
        if (memberPrincipal.getMemberId() != memberId) {
            throw new ErrorLogicException(ErrorCode.FORBIDDEN);
        }
        return new ResponseEntity<>(
                memberService.updateMember(patchDto, memberId),
                HttpStatus.OK);
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<?> deleteMember(@Positive @PathVariable long memberId,
                                          @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        // 로그인 유저와 정보가 다르다면 수정 불가 (접근 권한 X)
        if (memberPrincipal.getMemberId() != memberId) {
            throw new ErrorLogicException(ErrorCode.FORBIDDEN);
        }
        memberService.deleteMember(memberId);
        return new ResponseEntity<>("회원탈퇴 되었습니다", HttpStatus.NO_CONTENT);
    }

    @PostMapping("/follow")
    public ResponseEntity<?> follow(@AuthenticationPrincipal MemberPrincipal memberPrincipal,
                                    @RequestParam String type,
                                    @RequestParam(name = "op") long opMemberId) {
        switch (type) {
            case "up":
                memberService.followMember(memberPrincipal.getAccount(), opMemberId);
                break;
            case "down":
                memberService.unfollowMember(memberPrincipal.getAccount(), opMemberId);
                break;
            default:
                throw new IllegalArgumentException();
        }
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/profile")
    public ResponseEntity<?> uploadProfile(@AuthenticationPrincipal MemberPrincipal memberPrincipal,
                                           @RequestParam(name = "image") MultipartFile multipartFile) throws IOException {
        String imageUrl = fileService.upload(multipartFile, "profile");
        return new ResponseEntity<>(
                memberService.setProfileImage(memberPrincipal.getAccount(), imageUrl),
                HttpStatus.OK
        );
    }
}
