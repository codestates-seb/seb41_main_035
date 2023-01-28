package com.lookatme.server.comment.entity;

import com.lookatme.server.audit.BaseTimeEntity;
import com.lookatme.server.board.entity.Board;
import com.lookatme.server.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "comment")
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "content", nullable = false)
    private String content;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    private Comment() {

    }

    @Builder
    public Comment(final Long commentId,
                   final String content,
                   final Member member,
                   final Board board) {
        this.commentId = commentId;
        this.content = content;
        this.member = member;
        this.board = board;
    }

    public void changeContent(final String content) {
        this.content = content;
    }

    public void addMember(final Member member) {
        this.member = member;
    }

    public void addBoard(final Board board) {
        this.board = board;
        board.getComments().add(this);
    }
}
