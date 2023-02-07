package com.lookatme.server.likes.entity;

import com.lookatme.server.audit.BaseTimeEntity;
import com.lookatme.server.board.entity.Board;
import com.lookatme.server.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "likes")
public class Likes extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likes_id")
    private Long likesId;

    @Column(nullable = false)
    private boolean status; // true = 좋아요

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    private Likes() {

    }
    @Builder
    public Likes(final boolean status,
                 final Member member,
                 final Board board) {
        this.status = status;
        this.member = member;
        this.board = board;
    }
}
