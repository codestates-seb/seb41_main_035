package com.lookatme.server.board.entity;

import com.lookatme.server.audit.BaseTimeEntity;
import com.lookatme.server.comment.entity.Comment;
import com.lookatme.server.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NamedEntityGraph(
        name = "board-entity-graph",
        attributeNodes = {
                @NamedAttributeNode(value = "member")
        }
)
@Entity
@Getter
@Setter
@Table(name = "boards")
public class Board extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long boardId;

    @Column(nullable = false)
    private String userImage;

    @Column(nullable = false)
    private String content;

    private int likeCnt;

    @Transient
    private boolean like; // 회원이 해당 게시글을 좋아요 눌렀는지 유무 (테이블 반영 X)

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<BoardProduct> boardProducts = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "member_id",
            foreignKey = @ForeignKey(name = "FK_member")
    )
    private Member member;

    @OneToMany(mappedBy = "board")
    private List<Comment> comments = new ArrayList<>();

    public void updateBoard(String userImage, String content) {
        this.userImage = userImage;
        this.content = content;
    }

    public void setLikeStatusTrue() {
        this.like = true;
    }

    public Board() {
    }

    @Builder
    public Board(final long boardId,
                 final String userImage,
                 final String content,
                 final int likeCnt) {
        this.boardId = boardId;
        this.userImage = userImage;
        this.content = content;
        this.likeCnt = likeCnt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return boardId == board.boardId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(boardId);
    }
}
