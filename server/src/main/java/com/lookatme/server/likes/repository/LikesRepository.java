package com.lookatme.server.likes.repository;

import com.lookatme.server.board.entity.Board;
import com.lookatme.server.likes.entity.Likes;
import com.lookatme.server.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {
    Likes findByBoardAndMember(Board board, Member member);
}
