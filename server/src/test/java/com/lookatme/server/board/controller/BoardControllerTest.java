package com.lookatme.server.board.controller;

import com.google.gson.Gson;
import com.lookatme.server.auth.dto.MemberPrincipal;
import com.lookatme.server.auth.jwt.JwtTokenizer;
import com.lookatme.server.board.dto.BoardPatchDto;
import com.lookatme.server.board.dto.BoardPostDto;
import com.lookatme.server.board.dto.BoardResponseDto;
import com.lookatme.server.board.service.BoardService;
import com.lookatme.server.comment.dto.CommentResponseDtoV2;
import com.lookatme.server.config.CustomTestConfiguration;
import com.lookatme.server.member.dto.MemberDto;
import com.lookatme.server.member.entity.Account;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.entity.OauthPlatform;
import com.lookatme.server.member.mapper.MemberMapper;
import com.lookatme.server.member.mapper.MemberMapperImpl;
import com.lookatme.server.product.dto.BoardProductsResponseDto;
import com.lookatme.server.product.dto.ProductPatchDto;
import com.lookatme.server.product.dto.ProductPostDto;
import com.lookatme.server.rental.dto.RentalResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static com.lookatme.server.util.ApiDocumentUtils.getRequestPreprocessor;
import static com.lookatme.server.util.ApiDocumentUtils.getResponsePreprocessor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({
        CustomTestConfiguration.class,
        MemberMapperImpl.class,
        JwtTokenizer.class
})
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest({BoardController.class})
@AutoConfigureRestDocs
class BoardControllerTest {

    @MockBean
    private BoardService boardService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private Gson gson;

    @Autowired
    private MemberMapper memberMapper;

    private String accessToken;

    private Member savedMember;

    @BeforeEach
    void createAccessToken() {
        savedMember = Member.builder()
                .memberId(1L)
                .account(new Account("email@com", OauthPlatform.NONE))
                .nickname("nickname")
                .profileImageUrl("http://프사링크")
                .height(180)
                .weight(70)
                .build();
        accessToken = jwtTokenizer.delegateAccessToken(savedMember);
    }

    @DisplayName("게시글 조회")
    @Test
    void getBoardTest() throws Exception {
        // Given
        MemberDto.ResponseWithFollow memberResponse = memberMapper.memberToMemberResponseWithFollow(savedMember);
        BoardProductsResponseDto productResponse = createProductResponse();
        CommentResponseDtoV2 commentResponse = createCommentResponse(memberMapper.memberToSimpleMemberResponse(savedMember));
        BoardResponseDto boardResponse = createBoardResponse(
                memberResponse,
                List.of(productResponse),
                List.of(commentResponse)
        );
        given(boardService.findBoard(anyLong(), anyLong())).willReturn(boardResponse);

        // When
        ResultActions actions = mockMvc.perform(
                get("/boards/{boardId}", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
        );

        // Then
        actions.andExpect(status().isOk())
                .andDo(document(
                        "get-board",
                        getRequestPreprocessor(),
                        getResponsePreprocessor(),
                        pathParameters(
                                parameterWithName("boardId").description("게시글 번호")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("boardId").description("게시글 번호"),
                                        fieldWithPath("userImage").description("게시글 사진 링크"),
                                        fieldWithPath("content").description("게시글 본문"),
                                        fieldWithPath("createdDate").description("게시글 작성 시각"),
                                        fieldWithPath("updatedDate").description("게시글 수정 시각"),
                                        fieldWithPath("likeCnt").description("추천수"),
                                        fieldWithPath("like").description("게시글 좋아요 여부"),
                                        fieldWithPath("member.memberId").description("작성자 회원 번호"),
                                        fieldWithPath("member.email").description("작성자 이메일"),
                                        fieldWithPath("member.oauthPlatform").description("작성자 가입 플랫폼"),
                                        fieldWithPath("member.nickname").description("작성자 닉네임"),
                                        fieldWithPath("member.profileImageUrl").description("작성자 프로필 사진 링크"),
                                        fieldWithPath("member.height").description("작성자 키"),
                                        fieldWithPath("member.weight").description("작성자 몸무게"),
                                        fieldWithPath("member.followerCnt").description("작성자 팔로워 수"),
                                        fieldWithPath("member.followeeCnt").description("작성자 팔로우 수"),
                                        fieldWithPath("member.follow").description("내가 팔로우 한 회원인지 (액세스 토큰 소유자 기준)"),
                                        fieldWithPath("member.delete").description("탈퇴한 회원인지"),
                                        fieldWithPath("products[].productId").description("상품 번호"),
                                        fieldWithPath("products[].productName").description("상품명"),
                                        fieldWithPath("products[].productImage").description("상품 사진 링크"),
                                        fieldWithPath("products[].link").description("상품 구매 경로"),
                                        fieldWithPath("products[].category").description("상품 카테고리"),
                                        fieldWithPath("products[].brand").description("상품 브랜드"),
                                        fieldWithPath("products[].price").description("상품 가격"),
                                        fieldWithPath("products[].rental.rentalId").description("렌탈 번호"),
                                        fieldWithPath("products[].rental.size").description("렌탈 상품 사이즈"),
                                        fieldWithPath("products[].rental.rentalPrice").description("렌탈 가격"),
                                        fieldWithPath("products[].rental.available").description("렌탈 가능 여부"),
                                        fieldWithPath("comments[].commentId").description("댓글 번호"),
                                        fieldWithPath("comments[].content").description("댓글 내용"),
                                        fieldWithPath("comments[].createdDate").description("댓글 작성 시각"),
                                        fieldWithPath("comments[].updatedDate").description("댓글 수정 시각"),
                                        fieldWithPath("comments[].member.memberId").description("댓글 작성자 회원 번호"),
                                        fieldWithPath("comments[].member.nickname").description("댓글 작성자 닉네임"),
                                        fieldWithPath("comments[].member.profileImageUrl").description("댓글 작성자 프로필 사진"),
                                        fieldWithPath("comments[].member.delete").description("댓글 작성자 탈퇴 유무")
                                )
                        )
                ));

    }

    @DisplayName("게시글 목록 조회")
    @Test
    void getBoardsTest() throws Exception {
        // Given
        MemberDto.ResponseWithFollow memberResponse = memberMapper.memberToMemberResponseWithFollow(savedMember);
        BoardProductsResponseDto productResponse = createProductResponse();
        CommentResponseDtoV2 commentResponse = createCommentResponse(memberMapper.memberToSimpleMemberResponse(savedMember));
        BoardResponseDto boardResponse = createBoardResponse(
                memberResponse,
                List.of(productResponse),
                List.of(commentResponse)
        );
        int page = 1;
        int size = 10;
        List<BoardResponseDto> boardResponseList = List.of(boardResponse);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), boardResponseList.size());
        PageImpl<BoardResponseDto> boardPage = new PageImpl<>(boardResponseList.subList(start, end), pageRequest, boardResponseList.size());
        given(boardService.findBoards(page - 1, size, 1L)).willReturn(boardPage);

        // When
        ResultActions actions = mockMvc.perform(
                get("/boards")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("page", Integer.toString(page))
                        .param("size", Integer.toString(size))
                        .header("Authorization", accessToken)
        );

        // Then
        actions.andExpect(status().isOk())
                .andDo(document(
                        "get-boards",
                        getRequestPreprocessor(),
                        getResponsePreprocessor(),
                        responseFields(
                                List.of(
                                        fieldWithPath("data[].boardId").description("게시글 번호"),
                                        fieldWithPath("data[].userImage").description("게시글 사진 링크"),
                                        fieldWithPath("data[].content").description("게시글 본문"),
                                        fieldWithPath("data[].createdDate").description("게시글 작성 시각"),
                                        fieldWithPath("data[].updatedDate").description("게시글 수정 시각"),
                                        fieldWithPath("data[].likeCnt").description("추천수"),
                                        fieldWithPath("data[].like").description("게시글 좋아요 여부"),
                                        fieldWithPath("data[].member.memberId").description("작성자 회원 번호"),
                                        fieldWithPath("data[].member.email").description("작성자 이메일"),
                                        fieldWithPath("data[].member.oauthPlatform").description("작성자 가입 플랫폼"),
                                        fieldWithPath("data[].member.nickname").description("작성자 닉네임"),
                                        fieldWithPath("data[].member.profileImageUrl").description("작성자 프로필 사진 링크"),
                                        fieldWithPath("data[].member.height").description("작성자 키"),
                                        fieldWithPath("data[].member.weight").description("작성자 몸무게"),
                                        fieldWithPath("data[].member.followerCnt").description("작성자 팔로워 수"),
                                        fieldWithPath("data[].member.followeeCnt").description("작성자 팔로우 수"),
                                        fieldWithPath("data[].member.follow").description("내가 팔로우 한 회원인지 (액세스 토큰 소유자 기준)"),
                                        fieldWithPath("data[].member.delete").description("탈퇴한 회원인지"),
                                        fieldWithPath("data[].products[].productId").description("상품 번호"),
                                        fieldWithPath("data[].products[].productName").description("상품명"),
                                        fieldWithPath("data[].products[].productImage").description("상품 사진 링크"),
                                        fieldWithPath("data[].products[].link").description("상품 구매 경로"),
                                        fieldWithPath("data[].products[].category").description("상품 카테고리"),
                                        fieldWithPath("data[].products[].brand").description("상품 브랜드"),
                                        fieldWithPath("data[].products[].price").description("상품 가격"),
                                        fieldWithPath("data[].products[].rental.rentalId").description("렌탈 번호"),
                                        fieldWithPath("data[].products[].rental.size").description("렌탈 상품 사이즈"),
                                        fieldWithPath("data[].products[].rental.rentalPrice").description("렌탈 가격"),
                                        fieldWithPath("data[].products[].rental.available").description("렌탈 가능 여부"),
                                        fieldWithPath("data[].comments[].commentId").description("댓글 번호"),
                                        fieldWithPath("data[].comments[].content").description("댓글 내용"),
                                        fieldWithPath("data[].comments[].createdDate").description("댓글 작성 시각"),
                                        fieldWithPath("data[].comments[].updatedDate").description("댓글 수정 시각"),
                                        fieldWithPath("data[].comments[].member.memberId").description("댓글 작성자 회원 번호"),
                                        fieldWithPath("data[].comments[].member.nickname").description("댓글 작성자 닉네임"),
                                        fieldWithPath("data[].comments[].member.profileImageUrl").description("댓글 작성자 프로필 사진"),
                                        fieldWithPath("data[].comments[].member.delete").description("댓글 작성자 탈퇴 유무"),
                                        fieldWithPath("pageInfoDto.page").description("페이지"),
                                        fieldWithPath("pageInfoDto.size").description("페이지 당 데이터 개수"),
                                        fieldWithPath("pageInfoDto.totalElements").description("전체 데이터 개수"),
                                        fieldWithPath("pageInfoDto.totalPages").description("전체 페이지")
                                )
                        )
                ));

    }
    @DisplayName("게시글 등록")
    @Test
    void postBoardTest() throws Exception {
        // Given
        MemberDto.ResponseWithFollow memberResponse = memberMapper.memberToMemberResponseWithFollow(savedMember);
        BoardProductsResponseDto productResponse = createProductResponse();
        CommentResponseDtoV2 commentResponse = createCommentResponse(memberMapper.memberToSimpleMemberResponse(savedMember));
        BoardResponseDto boardResponse = createBoardResponse(
                memberResponse,
                List.of(productResponse),
                List.of(commentResponse)
        );
        given(boardService.createBoard(any(BoardPostDto.class), anyLong())).willReturn(boardResponse);
        MockMultipartFile testImage = new MockMultipartFile("image", "testImage.png", "image/png", "<<png data>>".getBytes());
        ProductPostDto productPostDto = new ProductPostDto(
                testImage,
                "상품명",
                "카테고리",
                "브랜드",
                "사이즈",
                "구매 링크",
                49900,
                9900,
                true
        );
        BoardPostDto post = new BoardPostDto(
                "게시글 본문",
                testImage,
                List.of(productPostDto)
        );
        String content = gson.toJson(post);

        // When
        ResultActions actions = mockMvc.perform(
                post("/boards")
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .content(content)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
        );

        // Then
        actions.andExpect(status().isCreated())
                .andDo(document(
                        "post-board",
                        getRequestPreprocessor(),
                        getResponsePreprocessor(),
                        requestFields(
                                List.of(
                                        fieldWithPath("content").description("게시글 본문"),
                                        fieldWithPath("userImage").description("게시글 사진"),
                                        fieldWithPath("userImage.name").ignored(),
                                        fieldWithPath("userImage.originalFilename").ignored(),
                                        fieldWithPath("userImage.contentType").ignored(),
                                        fieldWithPath("userImage.content").ignored(),
                                        fieldWithPath("products[].productImage").description("상품 사진"),
                                        fieldWithPath("products[].productImage.name").ignored(),
                                        fieldWithPath("products[].productImage.originalFilename").ignored(),
                                        fieldWithPath("products[].productImage.contentType").ignored(),
                                        fieldWithPath("products[].productImage.content").ignored(),
                                        fieldWithPath("products[].productName").description("상품명"),
                                        fieldWithPath("products[].category").description("카테고리"),
                                        fieldWithPath("products[].brand").description("브랜드"),
                                        fieldWithPath("products[].size").description("상품 사이즈"),
                                        fieldWithPath("products[].link").description("상품 구매 링크"),
                                        fieldWithPath("products[].price").description("상품 구매 가격"),
                                        fieldWithPath("products[].rentalPrice").description("상품 렌탈 가격"),
                                        fieldWithPath("products[].rental").description("렌탈 가능 여부")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("boardId").description("게시글 번호"),
                                        fieldWithPath("userImage").description("게시글 사진 링크"),
                                        fieldWithPath("content").description("게시글 본문"),
                                        fieldWithPath("createdDate").description("게시글 작성 시각"),
                                        fieldWithPath("updatedDate").description("게시글 수정 시각"),
                                        fieldWithPath("likeCnt").description("추천수"),
                                        fieldWithPath("like").description("게시글 좋아요 여부"),
                                        fieldWithPath("member.memberId").description("작성자 회원 번호"),
                                        fieldWithPath("member.email").description("작성자 이메일"),
                                        fieldWithPath("member.oauthPlatform").description("작성자 가입 플랫폼"),
                                        fieldWithPath("member.nickname").description("작성자 닉네임"),
                                        fieldWithPath("member.profileImageUrl").description("작성자 프로필 사진 링크"),
                                        fieldWithPath("member.height").description("작성자 키"),
                                        fieldWithPath("member.weight").description("작성자 몸무게"),
                                        fieldWithPath("member.followerCnt").description("작성자 팔로워 수"),
                                        fieldWithPath("member.followeeCnt").description("작성자 팔로우 수"),
                                        fieldWithPath("member.follow").description("내가 팔로우 한 회원인지 (액세스 토큰 소유자 기준)"),
                                        fieldWithPath("member.delete").description("탈퇴한 회원인지"),
                                        fieldWithPath("products[].productId").description("상품 번호"),
                                        fieldWithPath("products[].productName").description("상품명"),
                                        fieldWithPath("products[].productImage").description("상품 사진 링크"),
                                        fieldWithPath("products[].link").description("상품 구매 경로"),
                                        fieldWithPath("products[].category").description("상품 카테고리"),
                                        fieldWithPath("products[].brand").description("상품 브랜드"),
                                        fieldWithPath("products[].price").description("상품 가격"),
                                        fieldWithPath("products[].rental.rentalId").description("렌탈 번호"),
                                        fieldWithPath("products[].rental.size").description("렌탈 상품 사이즈"),
                                        fieldWithPath("products[].rental.rentalPrice").description("렌탈 가격"),
                                        fieldWithPath("products[].rental.available").description("렌탈 가능 여부"),
                                        fieldWithPath("comments[].commentId").description("댓글 번호"),
                                        fieldWithPath("comments[].content").description("댓글 내용"),
                                        fieldWithPath("comments[].createdDate").description("댓글 작성 시각"),
                                        fieldWithPath("comments[].updatedDate").description("댓글 수정 시각"),
                                        fieldWithPath("comments[].member.memberId").description("댓글 작성자 회원 번호"),
                                        fieldWithPath("comments[].member.nickname").description("댓글 작성자 닉네임"),
                                        fieldWithPath("comments[].member.profileImageUrl").description("댓글 작성자 프로필 사진"),
                                        fieldWithPath("comments[].member.delete").description("댓글 작성자 탈퇴 유무")
                                )
                        )
                ));
    }

    @DisplayName("게시글 수정")
    @Test
    void updateBoardTest() throws Exception {
        // Given
        MemberDto.ResponseWithFollow memberResponse = memberMapper.memberToMemberResponseWithFollow(savedMember);
        BoardProductsResponseDto productResponse = createProductResponse();
        CommentResponseDtoV2 commentResponse = createCommentResponse(memberMapper.memberToSimpleMemberResponse(savedMember));
        BoardResponseDto boardResponse = createBoardResponse(
                memberResponse,
                List.of(productResponse),
                List.of(commentResponse)
        );
        given(boardService.updateBoard(any(BoardPatchDto.class), anyLong(), anyLong())).willReturn(boardResponse);
        MockMultipartFile testImage = new MockMultipartFile("image", "testImage.png", "image/png", "<<png data>>".getBytes());
        ProductPatchDto productPatchDto = new ProductPatchDto(
                testImage,
                "상품명",
                "카테고리",
                "브랜드",
                "사이즈",
                "구매 링크",
                1L,
                49900,
                9900,
                true
        );
        BoardPatchDto post = new BoardPatchDto(
                "게시글 본문",
                testImage,
                List.of(productPatchDto)
        );
        String content = gson.toJson(post);

        // When
        ResultActions actions = mockMvc.perform(
                patch("/boards/{boardId}", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .content(content)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
        );

        // Then
        actions.andExpect(status().isOk())
                .andDo(document(
                        "patch-board",
                        getRequestPreprocessor(),
                        getResponsePreprocessor(),
                        pathParameters(
                                parameterWithName("boardId").description("게시글 번호")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("content").description("게시글 본문"),
                                        fieldWithPath("userImage").description("게시글 사진"),
                                        fieldWithPath("userImage.name").ignored(),
                                        fieldWithPath("userImage.originalFilename").ignored(),
                                        fieldWithPath("userImage.contentType").ignored(),
                                        fieldWithPath("userImage.content").ignored(),
                                        fieldWithPath("products[].productId").description("상품 번호"),
                                        fieldWithPath("products[].productImage").description("상품 사진"),
                                        fieldWithPath("products[].productImage.name").ignored(),
                                        fieldWithPath("products[].productImage.originalFilename").ignored(),
                                        fieldWithPath("products[].productImage.contentType").ignored(),
                                        fieldWithPath("products[].productImage.content").ignored(),
                                        fieldWithPath("products[].productName").description("상품명"),
                                        fieldWithPath("products[].category").description("카테고리"),
                                        fieldWithPath("products[].brand").description("브랜드"),
                                        fieldWithPath("products[].size").description("상품 사이즈"),
                                        fieldWithPath("products[].link").description("상품 구매 링크"),
                                        fieldWithPath("products[].price").description("상품 구매 가격"),
                                        fieldWithPath("products[].rentalPrice").description("상품 렌탈 가격"),
                                        fieldWithPath("products[].rental").description("렌탈 가능 여부")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("boardId").description("게시글 번호"),
                                        fieldWithPath("userImage").description("게시글 사진 링크"),
                                        fieldWithPath("content").description("게시글 본문"),
                                        fieldWithPath("createdDate").description("게시글 작성 시각"),
                                        fieldWithPath("updatedDate").description("게시글 수정 시각"),
                                        fieldWithPath("likeCnt").description("추천수"),
                                        fieldWithPath("like").description("게시글 좋아요 여부"),
                                        fieldWithPath("member.memberId").description("작성자 회원 번호"),
                                        fieldWithPath("member.email").description("작성자 이메일"),
                                        fieldWithPath("member.oauthPlatform").description("작성자 가입 플랫폼"),
                                        fieldWithPath("member.nickname").description("작성자 닉네임"),
                                        fieldWithPath("member.profileImageUrl").description("작성자 프로필 사진 링크"),
                                        fieldWithPath("member.height").description("작성자 키"),
                                        fieldWithPath("member.weight").description("작성자 몸무게"),
                                        fieldWithPath("member.followerCnt").description("작성자 팔로워 수"),
                                        fieldWithPath("member.followeeCnt").description("작성자 팔로우 수"),
                                        fieldWithPath("member.follow").description("내가 팔로우 한 회원인지 (액세스 토큰 소유자 기준)"),
                                        fieldWithPath("member.delete").description("탈퇴한 회원인지"),
                                        fieldWithPath("products[].productId").description("상품 번호"),
                                        fieldWithPath("products[].productName").description("상품명"),
                                        fieldWithPath("products[].productImage").description("상품 사진 링크"),
                                        fieldWithPath("products[].link").description("상품 구매 경로"),
                                        fieldWithPath("products[].category").description("상품 카테고리"),
                                        fieldWithPath("products[].brand").description("상품 브랜드"),
                                        fieldWithPath("products[].price").description("상품 가격"),
                                        fieldWithPath("products[].rental.rentalId").description("렌탈 번호"),
                                        fieldWithPath("products[].rental.size").description("렌탈 상품 사이즈"),
                                        fieldWithPath("products[].rental.rentalPrice").description("렌탈 가격"),
                                        fieldWithPath("products[].rental.available").description("렌탈 가능 여부"),
                                        fieldWithPath("comments[].commentId").description("댓글 번호"),
                                        fieldWithPath("comments[].content").description("댓글 내용"),
                                        fieldWithPath("comments[].createdDate").description("댓글 작성 시각"),
                                        fieldWithPath("comments[].updatedDate").description("댓글 수정 시각"),
                                        fieldWithPath("comments[].member.memberId").description("댓글 작성자 회원 번호"),
                                        fieldWithPath("comments[].member.nickname").description("댓글 작성자 닉네임"),
                                        fieldWithPath("comments[].member.profileImageUrl").description("댓글 작성자 프로필 사진"),
                                        fieldWithPath("comments[].member.delete").description("댓글 작성자 탈퇴 유무")
                                )
                        )
                ));
    }

    @DisplayName("게시글 삭제")
    @Test
    void deleteBoardTest() throws Exception {
        // When
        ResultActions actions = mockMvc.perform(
                delete("/boards/{boardId}", 1L)
                        .header("Authorization", accessToken)
        );

        // Then
        actions.andExpect(status().isNoContent())
                .andDo(document(
                        "delete-board",
                        getRequestPreprocessor(),
                        getResponsePreprocessor(),
                        pathParameters(
                                parameterWithName("boardId").description("게시글 번호")
                        )
                ));
    }

    @DisplayName("게시글 좋아요")
    @Test
    void likeBoardTest() throws Exception{
        MemberDto.ResponseWithFollow memberResponse = memberMapper.memberToMemberResponseWithFollow(savedMember);
        BoardProductsResponseDto productResponse = createProductResponse();
        CommentResponseDtoV2 commentResponse = createCommentResponse(memberMapper.memberToSimpleMemberResponse(savedMember));

        BoardResponseDto boardResponse = BoardResponseDto.builder()
                .boardId(1L)
                .userImage("게시글 사진 링크")
                .content("게시글 본문")
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .likeCnt(1)
                .like(false)
                .member(memberResponse)
                .products(List.of(productResponse))
                .comments(List.of(commentResponse))
                .build();

        given(boardService.likeBoard(Mockito.any(MemberPrincipal.class), Mockito.anyLong())).willReturn(boardResponse);

        // When
        ResultActions actions = mockMvc.perform(
                post("/boards/{boardId}/like", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("Authorization", accessToken)
        );

        // Then
        actions.andExpect(status().isOk())
                .andDo(document(
                        "like-board",
                        getRequestPreprocessor(),
                        getResponsePreprocessor(),
                        pathParameters(
                                parameterWithName("boardId").description("게시글 번호")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("boardId").description("게시글 번호"),
                                        fieldWithPath("userImage").description("게시글 사진 링크"),
                                        fieldWithPath("content").description("게시글 본문"),
                                        fieldWithPath("createdDate").description("게시글 작성 시각"),
                                        fieldWithPath("updatedDate").description("게시글 수정 시각"),
                                        fieldWithPath("likeCnt").description("추천수"),
                                        fieldWithPath("like").description("게시글 좋아요 여부"),
                                        fieldWithPath("member.memberId").description("작성자 회원 번호"),
                                        fieldWithPath("member.email").description("작성자 이메일"),
                                        fieldWithPath("member.oauthPlatform").description("작성자 가입 플랫폼"),
                                        fieldWithPath("member.nickname").description("작성자 닉네임"),
                                        fieldWithPath("member.profileImageUrl").description("작성자 프로필 사진 링크"),
                                        fieldWithPath("member.height").description("작성자 키"),
                                        fieldWithPath("member.weight").description("작성자 몸무게"),
                                        fieldWithPath("member.followerCnt").description("작성자 팔로워 수"),
                                        fieldWithPath("member.followeeCnt").description("작성자 팔로우 수"),
                                        fieldWithPath("member.follow").description("내가 팔로우 한 회원인지 (액세스 토큰 소유자 기준)"),
                                        fieldWithPath("member.delete").description("탈퇴한 회원인지"),
                                        fieldWithPath("products[].productId").description("상품 번호"),
                                        fieldWithPath("products[].productName").description("상품명"),
                                        fieldWithPath("products[].productImage").description("상품 사진 링크"),
                                        fieldWithPath("products[].link").description("상품 구매 경로"),
                                        fieldWithPath("products[].category").description("상품 카테고리"),
                                        fieldWithPath("products[].brand").description("상품 브랜드"),
                                        fieldWithPath("products[].price").description("상품 가격"),
                                        fieldWithPath("products[].rental.rentalId").description("렌탈 번호"),
                                        fieldWithPath("products[].rental.size").description("렌탈 상품 사이즈"),
                                        fieldWithPath("products[].rental.rentalPrice").description("렌탈 가격"),
                                        fieldWithPath("products[].rental.available").description("렌탈 가능 여부"),
                                        fieldWithPath("comments[].commentId").description("댓글 번호"),
                                        fieldWithPath("comments[].content").description("댓글 내용"),
                                        fieldWithPath("comments[].createdDate").description("댓글 작성 시각"),
                                        fieldWithPath("comments[].updatedDate").description("댓글 수정 시각"),
                                        fieldWithPath("comments[].member.memberId").description("댓글 작성자 회원 번호"),
                                        fieldWithPath("comments[].member.nickname").description("댓글 작성자 닉네임"),
                                        fieldWithPath("comments[].member.profileImageUrl").description("댓글 작성자 프로필 사진"),
                                        fieldWithPath("comments[].member.delete").description("댓글 작성자 탈퇴 유무")
                                )
                        )
                ));

    }

    private BoardResponseDto createBoardResponse(
            MemberDto.ResponseWithFollow member,
            List<BoardProductsResponseDto> products,
            List<CommentResponseDtoV2> comments) {
        return BoardResponseDto.builder()
                .boardId(1L)
                .userImage("게시글 사진 링크")
                .content("게시글 본문")
                .createdDate(LocalDateTime.now())
                .updatedDate(LocalDateTime.now())
                .likeCnt(0)
                .like(false)
                .member(member)
                .products(products)
                .comments(comments).build();
    }

    private BoardProductsResponseDto createProductResponse() {
        return new BoardProductsResponseDto(
                1L,
                "상품명",
                "상품 사진 링크",
                "상품 구매 사이트 링크",
                "카테고리명",
                "브랜드명",
                199000,
                new RentalResponseDto(
                        1L,
                        "사이즈",
                        9900,
                        true
                )
        );
    }


    private CommentResponseDtoV2 createCommentResponse(MemberDto.SimpleResponse member) {
        return new CommentResponseDtoV2(
                1L,
                "댓글 내용",
                LocalDateTime.now(),
                LocalDateTime.now(),
                member
        );
    }
}