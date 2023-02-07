package com.lookatme.server.likes.service;

import com.lookatme.server.board.entity.Board;
import com.lookatme.server.likes.entity.Likes;
import com.lookatme.server.likes.repository.LikesRepository;
import com.lookatme.server.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

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

    /**
     * 해당 게시글이 해당 회원이 좋아요를 누른 게시글인지 유무를 리턴
     */
    @Transactional(readOnly = true)
    public boolean isLikePost(final Member member, final Board board) {
        return likesRepository.existsByBoardAndMember(board, member);
    }

    /**
     * 회원이 좋아요 누른 게시글 ID Set 리턴
     */
    @Transactional(readOnly = true)
    public Set<Long> getLikeBoardIdSet(final Member member, final Pageable pageable) {
        Page<Likes> likePage = likesRepository.findByMember(member, pageable);
        return likePage.getContent().stream()
                .map(likes -> likes.getBoard().getBoardId())
                .collect(Collectors.toSet());
    }
}
