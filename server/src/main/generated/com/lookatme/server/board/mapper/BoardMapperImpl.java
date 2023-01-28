package com.lookatme.server.board.mapper;

import com.lookatme.server.board.dto.BoardPatchDto;
import com.lookatme.server.board.dto.BoardPostDto;
import com.lookatme.server.board.dto.BoardResponseDto;
import com.lookatme.server.board.entity.Board;
import com.lookatme.server.board.entity.Board.BoardBuilder;
import com.lookatme.server.board.entity.BoardProduct;
import com.lookatme.server.comment.dto.CommentResponseDtoV2;
import com.lookatme.server.comment.entity.Comment;
import com.lookatme.server.member.dto.MemberDto.ResponseWithFollow;
import com.lookatme.server.member.dto.MemberDto.SimpleResponse;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.entity.OauthPlatform;
import com.lookatme.server.product.dto.BoardProductsResponseDto;
import com.lookatme.server.rental.dto.RentalResponseDto;
import com.lookatme.server.rental.entity.Rental;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-01-28T15:32:35+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.16.1 (Eclipse Adoptium)"
)
@Component
public class BoardMapperImpl implements BoardMapper {

    @Override
    public Board boardPostToBoard(BoardPostDto post) {
        if ( post == null ) {
            return null;
        }

        BoardBuilder board = Board.builder();

        board.content( post.getContent() );

        return board.build();
    }

    @Override
    public Board boardPatchToBoard(BoardPatchDto patch) {
        if ( patch == null ) {
            return null;
        }

        BoardBuilder board = Board.builder();

        board.content( patch.getContent() );

        return board.build();
    }

    @Override
    public BoardResponseDto boardToBoardResponse(Board board) {
        if ( board == null ) {
            return null;
        }

        BoardResponseDto boardResponseDto = new BoardResponseDto();

        boardResponseDto.setProducts( boardProductListToBoardProductsResponseDtoList( board.getBoardProducts() ) );
        boardResponseDto.setBoardId( (int) board.getBoardId() );
        boardResponseDto.setUserImage( board.getUserImage() );
        boardResponseDto.setContent( board.getContent() );
        boardResponseDto.setCreatedDate( board.getCreatedDate() );
        boardResponseDto.setUpdatedDate( board.getUpdatedDate() );
        boardResponseDto.setLikeCnt( board.getLikeCnt() );
        boardResponseDto.setMember( memberToResponseWithFollow( board.getMember() ) );
        boardResponseDto.setComments( commentListToCommentResponseDtoV2List( board.getComments() ) );

        return boardResponseDto;
    }

    @Override
    public List<BoardResponseDto> boardsToBoardResponseDtos(List<Board> boards) {
        if ( boards == null ) {
            return null;
        }

        List<BoardResponseDto> list = new ArrayList<BoardResponseDto>( boards.size() );
        for ( Board board : boards ) {
            list.add( boardToBoardResponse( board ) );
        }

        return list;
    }

    protected RentalResponseDto rentalToRentalResponseDto(Rental rental) {
        if ( rental == null ) {
            return null;
        }

        RentalResponseDto rentalResponseDto = new RentalResponseDto();

        rentalResponseDto.setRentalId( (int) rental.getRentalId() );
        rentalResponseDto.setSize( rental.getSize() );
        rentalResponseDto.setRentalPrice( rental.getRentalPrice() );
        rentalResponseDto.setAvailable( rental.isAvailable() );

        return rentalResponseDto;
    }

    protected BoardProductsResponseDto boardProductToBoardProductsResponseDto(BoardProduct boardProduct) {
        if ( boardProduct == null ) {
            return null;
        }

        BoardProductsResponseDto boardProductsResponseDto = new BoardProductsResponseDto();

        boardProductsResponseDto.setProductId( boardProduct.getProductId() );
        boardProductsResponseDto.setProductName( boardProduct.getProductName() );
        boardProductsResponseDto.setProductImage( boardProduct.getProductImage() );
        boardProductsResponseDto.setLink( boardProduct.getLink() );
        boardProductsResponseDto.setCategory( boardProduct.getCategory() );
        boardProductsResponseDto.setBrand( boardProduct.getBrand() );
        boardProductsResponseDto.setPrice( boardProduct.getPrice() );
        boardProductsResponseDto.setRental( rentalToRentalResponseDto( boardProduct.getRental() ) );

        return boardProductsResponseDto;
    }

    protected List<BoardProductsResponseDto> boardProductListToBoardProductsResponseDtoList(List<BoardProduct> list) {
        if ( list == null ) {
            return null;
        }

        List<BoardProductsResponseDto> list1 = new ArrayList<BoardProductsResponseDto>( list.size() );
        for ( BoardProduct boardProduct : list ) {
            list1.add( boardProductToBoardProductsResponseDto( boardProduct ) );
        }

        return list1;
    }

    protected ResponseWithFollow memberToResponseWithFollow(Member member) {
        if ( member == null ) {
            return null;
        }

        long memberId = 0L;
        String email = null;
        OauthPlatform oauthPlatform = null;
        String nickname = null;
        String profileImageUrl = null;
        int height = 0;
        int weight = 0;
        int followerCnt = 0;
        int followeeCnt = 0;
        boolean follow = false;
        boolean delete = false;

        memberId = member.getMemberId();
        email = member.getEmail();
        oauthPlatform = member.getOauthPlatform();
        nickname = member.getNickname();
        profileImageUrl = member.getProfileImageUrl();
        height = member.getHeight();
        weight = member.getWeight();
        followerCnt = member.getFollowerCnt();
        followeeCnt = member.getFolloweeCnt();
        follow = member.isFollow();
        delete = member.isDelete();

        ResponseWithFollow responseWithFollow = new ResponseWithFollow( memberId, email, oauthPlatform, nickname, profileImageUrl, height, weight, followerCnt, followeeCnt, follow, delete );

        return responseWithFollow;
    }

    protected SimpleResponse memberToSimpleResponse(Member member) {
        if ( member == null ) {
            return null;
        }

        long memberId = 0L;
        String nickname = null;
        String profileImageUrl = null;
        boolean delete = false;

        memberId = member.getMemberId();
        nickname = member.getNickname();
        profileImageUrl = member.getProfileImageUrl();
        delete = member.isDelete();

        SimpleResponse simpleResponse = new SimpleResponse( memberId, nickname, profileImageUrl, delete );

        return simpleResponse;
    }

    protected CommentResponseDtoV2 commentToCommentResponseDtoV2(Comment comment) {
        if ( comment == null ) {
            return null;
        }

        CommentResponseDtoV2 commentResponseDtoV2 = new CommentResponseDtoV2();

        commentResponseDtoV2.setCommentId( comment.getCommentId() );
        commentResponseDtoV2.setContent( comment.getContent() );
        commentResponseDtoV2.setCreatedDate( comment.getCreatedDate() );
        commentResponseDtoV2.setUpdatedDate( comment.getUpdatedDate() );
        commentResponseDtoV2.setMember( memberToSimpleResponse( comment.getMember() ) );

        return commentResponseDtoV2;
    }

    protected List<CommentResponseDtoV2> commentListToCommentResponseDtoV2List(List<Comment> list) {
        if ( list == null ) {
            return null;
        }

        List<CommentResponseDtoV2> list1 = new ArrayList<CommentResponseDtoV2>( list.size() );
        for ( Comment comment : list ) {
            list1.add( commentToCommentResponseDtoV2( comment ) );
        }

        return list1;
    }
}
