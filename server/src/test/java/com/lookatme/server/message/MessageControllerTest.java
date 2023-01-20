package com.lookatme.server.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lookatme.server.auth.dto.MemberPrincipal;
import com.lookatme.server.auth.jwt.JwtTokenizer;
import com.lookatme.server.auth.utils.MemberAuthorityUtils;
import com.lookatme.server.config.CustomTestConfiguration;
import com.lookatme.server.member.entity.Account;
import com.lookatme.server.common.dto.MultiResponseDto;
import com.lookatme.server.config.WithAuthMember;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.message.controller.MessageController;
import com.lookatme.server.message.dto.MessagePostDto;
import com.lookatme.server.message.dto.MessageResponseDto;
import com.lookatme.server.message.entity.Message;
import com.lookatme.server.message.service.MessageService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({
        CustomTestConfiguration.class,
        MemberAuthorityUtils.class,
        JwtTokenizer.class
})
@WebMvcTest(MessageController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class MessageControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void postMessageTest() throws Exception {
        //given
        MessagePostDto messagePostDto = MessagePostDto.builder()
                .content("메시지 내용")
                .receiverNickname("받는 사람 닉네임")
                .build();

        Member receiver = Member.builder()
                .memberId(1L)
                .account(new Account("email@com"))
                .nickname("메시지 발신자 닉네임")
                .profileImageUrl("메시지 발신자 프로필 사진 URL")
                .height(180)
                .weight(70)
                .build();

        Member sender = Member.builder()
                .memberId(2L)
                .account(new Account("email2@com"))
                .nickname("메시지 수신자 닉네임")
                .profileImageUrl("메시지 수신자 프로필 사진 URL")
                .height(180)
                .weight(70)
                .build();

        String content = objectMapper.writeValueAsString(messagePostDto);

        Message message = Message.builder()
                .messageId(1L)
                .content(messagePostDto.getContent())
                .receiver(receiver)
                .build();

        message.addReceiver(receiver);
        message.addSender(sender);

        given(messageService.createMessage(Mockito.any(MessagePostDto.class), Mockito.any())).willReturn(message);

        //when
        ResultActions actions =
                mockMvc.perform(post("/message")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                );
        //then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.messageId").value(message.getMessageId()))
                .andExpect(jsonPath("$.content").value(message.getContent()))
                .andDo(document("send-message",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        requestFields(
                                List.of(
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("메시지 내용"),
                                        fieldWithPath("receiverNickname").type(JsonFieldType.STRING).description("받는 사람 닉네임")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("messageId").type(JsonFieldType.NUMBER).description("메시지 식별자"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("메시지 내용"),
                                        fieldWithPath("createdAt").type(JsonFieldType.NULL).description("메시지 작성일"),
                                        fieldWithPath("senderNickname").type(JsonFieldType.STRING).description("메시지 발신자 닉네임"),
                                        fieldWithPath("senderProfileImageUrl").type(JsonFieldType.STRING).description("메시지 발신자 프로필 사진 링크"),
                                        fieldWithPath("receiverNickname").type(JsonFieldType.STRING).description("메시지 수신자 닉네임"),
                                        fieldWithPath("receiverProfileImageUrl").type(JsonFieldType.STRING).description("메시지 수신자 프로필 사진 링크")
                                )
                        )
                ));
    }

    @Test
    public void getMessageTest() throws Exception {
        //given
        Long messageId = 1L;

        Member receiver = Member.builder()
                .memberId(1L)
                .account(new Account("email@com"))
                .nickname("메시지 발신자 닉네임")
                .profileImageUrl("메시지 수신자 프로필 사진 URL")
                .height(180)
                .weight(70)
                .build();

        Member sender = Member.builder()
                .memberId(2L)
                .account(new Account("email2@com"))
                .nickname("메시지 수신자 닉네임")
                .profileImageUrl("메시지 발신자 프로필 사진 URL")
                .height(180)
                .weight(70)
                .build();

        Message message = Message.builder()
                .messageId(1L)
                .content("메시지 내용")
                .receiver(receiver)
                .build();

        message.addReceiver(receiver);
        message.addSender(sender);

        given(messageService.getMessage(Mockito.anyLong())).willReturn(message);

        //when
        ResultActions actions =
                mockMvc.perform(get("/message/{messageId}", messageId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                );
        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.messageId").value(message.getMessageId()))
                .andExpect(jsonPath("$.content").value(message.getContent()))
                .andDo(document("get-message",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("messageId").description("메시지 식별자")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("messageId").type(JsonFieldType.NUMBER).description("메시지 식별자"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("메시지 내용"),
                                        fieldWithPath("createdAt").type(JsonFieldType.NULL).description("메시지 작성일"),
                                        fieldWithPath("senderNickname").type(JsonFieldType.STRING).description("메시지 발신자 닉네임"),
                                        fieldWithPath("senderProfileImageUrl").type(JsonFieldType.STRING).description("메시지 발신자 프로필 사진 링크"),
                                        fieldWithPath("receiverNickname").type(JsonFieldType.STRING).description("메시지 수신자 닉네임"),
                                        fieldWithPath("receiverProfileImageUrl").type(JsonFieldType.STRING).description("메시지 수신자 프로필 사진 링크")
                                )
                        )
                ));
    }

    @Test
    @WithAuthMember(memberId = 1L)
    public void getMessagesTest() throws Exception {
        //given
        Long member1Id = 1L;
        Member member1 = Member.builder()
                .memberId(1L)
                .account(new Account("email@com"))
                .nickname("메시지 발신자 닉네임")
                .profileImageUrl("메시지 수신자 프로필 사진 URL")
                .height(180)
                .weight(70)
                .build();

        Long member2Id = 2L;
        Member member2 = Member.builder()
                .memberId(2L)
                .account(new Account("email2@com"))
                .nickname("메시지 수신자 닉네임")
                .profileImageUrl("메시지 발신자 프로필 사진 URL")
                .height(180)
                .weight(70)
                .build();

        Long member3Id = 3L;
        Member member3 = Member.builder()
                .memberId(3L)
                .account(new Account("email3@com"))
                .nickname("메시지 수신자 닉네임")
                .profileImageUrl("메시지 발신자 프로필 사진 URL")
                .height(180)
                .weight(70)
                .build();

        Message message1 = Message.builder()
                .messageId(1L)
                .content("메시지 내용")
                .sender(member2)
                .receiver(member1)
                .build();

        message1.addReceiver(member1);
        message1.addSender(member2);

        Message message2 = Message.builder()
                .messageId(2L)
                .content("메시지 내용")
                .sender(member3)
                .receiver(member1)
                .build();

        message2.addReceiver(member2);
        message2.addSender(member3);

        Message message3 = Message.builder()
                .messageId(3L)
                .content("메시지 내용")
                .sender(member1)
                .receiver(member1)
                .build();

        message2.addReceiver(member3);
        message2.addSender(member1);

        Message message4 = Message.builder()
                .messageId(4L)
                .content("메시지 내용")
                .sender(member3)
                .receiver(member1)
                .build();

        message2.addReceiver(member1);
        message2.addSender(member3);

        List<Message> messages = List.of(message3, message4);

        PageImpl<Message> messagePage = new PageImpl<>(messages,
                PageRequest.of(0, 10, Sort.by("createdAt")), 2);

        MessageResponseDto messageResponseDto = MessageResponseDto.builder()
                .messageId(message3.getMessageId())
                .content(message3.getContent())
                .createdAt(message3.getCreatedAt())
                .senderNickname(message3.getSender().getNickname())
                .senderProfileImageUrl(message3.getSender().getProfileImageUrl())
                .receiverNickname(message3.getReceiver().getNickname())
                .receiverProfileImageUrl(message3.getReceiver().getProfileImageUrl())
                .build();

        MessageResponseDto messageResponseDto2 = MessageResponseDto.builder()
                .messageId(message4.getMessageId())
                .content(message4.getContent())
                .createdAt(message4.getCreatedAt())
                .senderNickname(message4.getSender().getNickname())
                .senderProfileImageUrl(message4.getSender().getProfileImageUrl())
                .receiverNickname(message4.getReceiver().getNickname())
                .receiverProfileImageUrl(message4.getReceiver().getProfileImageUrl())
                .build();

        List<MessageResponseDto> messageResponseDtos = List.of(messageResponseDto, messageResponseDto2);

        given(messageService.getMessages(Mockito.any(), Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt())).willReturn(new MultiResponseDto<> (messageResponseDtos, messagePage));

        //when
        ResultActions actions =
                mockMvc.perform(get("/message/received/{memberId}", member3Id)
                        .param("page", "1")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                );
        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].messageId").value(message3.getMessageId()))
                .andExpect(jsonPath("$.data[0].content").value(message3.getContent()))
                .andExpect(jsonPath("$.data[1].messageId").value(message4.getMessageId()))
                .andExpect(jsonPath("$.data[1].content").value(message4.getContent()))
                .andDo(document("get-messages",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        requestParameters(
                                parameterWithName("page").description("페이지 번호"),
                                parameterWithName("size").description("페이지 크기")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("data").type(JsonFieldType.ARRAY).description("메시지 목록"),
                                        fieldWithPath("data[].messageId").type(JsonFieldType.NUMBER).description("메시지 식별자"),
                                        fieldWithPath("data[].content").type(JsonFieldType.STRING).description("메시지 내용"),
                                        fieldWithPath("data[].createdAt").type(JsonFieldType.NULL).description("메시지 작성일"),
                                        fieldWithPath("data[].senderNickname").type(JsonFieldType.STRING).description("메시지 발신자 닉네임"),
                                        fieldWithPath("data[].senderProfileImageUrl").type(JsonFieldType.STRING).description("메시지 발신자 프로필 사진 링크"),
                                        fieldWithPath("data[].receiverNickname").type(JsonFieldType.STRING).description("메시지 수신자 닉네임"),
                                        fieldWithPath("data[].receiverProfileImageUrl").type(JsonFieldType.STRING).description("메시지 수신자 프로필 사진 링크"),
                                        fieldWithPath("pageInfoDto").type(JsonFieldType.OBJECT).description("페이지 정보"),
                                        fieldWithPath("pageInfoDto.page").type(JsonFieldType.NUMBER).description("현재 페이지"),
                                        fieldWithPath("pageInfoDto.size").type(JsonFieldType.NUMBER).description("요청 페이지 사이즈"),
                                        fieldWithPath("pageInfoDto.totalElements").type(JsonFieldType.NUMBER).description("전체 개체수"),
                                        fieldWithPath("pageInfoDto.totalPages").type(JsonFieldType.NUMBER).description("전체 페이지")

                                )
                        )
                ));
    }

    @Test
    public void deleteMessageByReceiverTest() throws Exception {
        //given
        Long messageId = 1L;
        doNothing().when(messageService).deleteMessageByReceiver(Mockito.anyLong(), Mockito.any(MemberPrincipal.class));

        //when
        ResultActions actions =
                mockMvc.perform(delete("/message/received/{messageId}", messageId));
        //then
        actions
                .andExpect(status().isOk())
                .andDo(document("delete-message-by-receiver",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("messageId").description("메시지 식별자")
                        )
                ));
    }

    @Test
    public void deleteMessageBySenderTest() throws Exception {
        //given
        Long messageId = 1L;
        doNothing().when(messageService).deleteMessageBySender(Mockito.anyLong(), Mockito.any(MemberPrincipal.class));

        //when
        ResultActions actions =
                mockMvc.perform(delete("/message/sent/{messageId}", messageId));
        //then
        actions
                .andExpect(status().isOk())
                .andDo(document("delete-message-by-sender",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                        pathParameters(
                                parameterWithName("messageId").description("메시지 식별자")
                        )
                ));
    }
}
