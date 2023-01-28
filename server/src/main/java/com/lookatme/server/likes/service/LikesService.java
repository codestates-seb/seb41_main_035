package com.lookatme.server.likes.service;

import com.lookatme.server.board.entity.Board;
import com.lookatme.server.likes.entity.Likes;
import com.lookatme.server.likes.repository.LikesRepository;
import com.lookatme.server.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikesService {
    private final LikesRepository likesRepository;
    @Transactional
    public void like(final Member member, final Board board) {
        Likes likes = new Likes(true, member, board);
        likesRepository.save(likes);
    }

    @Transactional
    public void unlike(final Member member, final Board board) {
        likesRepository.delete(likesRepository.findByBoardAndMember(board, member));
    }

}
