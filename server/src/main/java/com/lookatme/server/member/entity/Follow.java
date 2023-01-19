package com.lookatme.server.member.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long followId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_member_id")
    Member from; // 팔로우를 누른 사람

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_member_id")
    Member to; // 팔로우가 눌린 사람

    @Column(name = "follow_start_date", updatable = false)
    private LocalDateTime followDate;

    public Follow(Member from, Member to) {
        this.from = from;
        this.to = to;
        this.followDate = LocalDateTime.now();
    }
}
