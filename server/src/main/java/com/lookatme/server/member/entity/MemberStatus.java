package com.lookatme.server.member.entity;

import lombok.Getter;

public enum MemberStatus {
    MEMBER_ACTIVE("활동중"),
    MEMBER_LOCKED("계정 잠김"),
    MEMBER_SLEEP("휴면 상태"),
    MEMBER_WITHDRAWAL("탈퇴 상태");

    @Getter
    private String status;

    MemberStatus(String status) {
        this.status = status;
    }

}
