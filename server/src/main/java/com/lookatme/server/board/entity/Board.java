package com.lookatme.server.board.entity;

import com.lookatme.server.audit.BaseTimeEntity;
import com.lookatme.server.comment.entity.Comment;
import com.lookatme.server.entity.BoardProduct;
import com.lookatme.server.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "boards")
public class Board extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int boardId;

    @Column(nullable = false)
    private String userImage;

    @Column(nullable = false)
    private String content;

    private int likeCnt;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<BoardProduct> boardProducts = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "board")
    private List<Comment> comments = new ArrayList<>();

    public Board() {
    }

    @Builder
    public Board(final int boardId,
                 final String userImage,
                 final String content,
                 final int likeCnt) {
        this.boardId = boardId;
        this.userImage = userImage;
        this.content = content;
        this.likeCnt = likeCnt;
    }
}
