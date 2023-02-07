package com.lookatme.server.member.mapper;

import com.lookatme.server.member.dto.MemberDto;
import com.lookatme.server.member.entity.Member;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member memberPostDtoToMember(MemberDto.Post memberPostDto);
    MemberDto.Response memberToMemberResponse(Member member);
    MemberDto.ResponseWithFollow memberToMemberResponseWithFollow(Member member);

    MemberDto.SimpleResponse memberToSimpleMemberResponse(Member member);
    List<MemberDto.Response> memberListToMemberResponseList(List<Member> memberList);
}
