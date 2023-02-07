package com.lookatme.server.message.dto;

import com.lookatme.server.message.entity.Message;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MessageResponseDto {
    private Long messageId;
    private String content;
    private LocalDateTime createdDate;
    private Long messageRoom;
    private Long senderId;
    private String senderNickname;
    private String senderProfileImageUrl;
    private Long receiverId;
    private String receiverNickname;
    private String receiverProfileImageUrl;

    @Builder
    public MessageResponseDto(final Long messageId,
                              final String content,
                              final LocalDateTime createdDate,
                              final Long messageRoom,
                              final Long senderId,
                              final String senderNickname,
                              final String senderProfileImageUrl,
                              final Long receiverId,
                              final String receiverNickname,
                              final String receiverProfileImageUrl) {
        this.messageId = messageId;
        this.content = content;
        this.createdDate = createdDate;
        this.messageRoom = messageRoom;
        this.senderId = senderId;
        this.senderNickname = senderNickname;
        this.senderProfileImageUrl = senderProfileImageUrl;
        this.receiverId = receiverId;
        this.receiverNickname = receiverNickname;
        this.receiverProfileImageUrl = receiverProfileImageUrl;
    }

    public static MessageResponseDto of(final Message message) {
        return MessageResponseDto.builder()
                .messageId(message.getMessageId())
                .content(message.getContent())
                .createdDate(message.getCreatedDate())
                .messageRoom(message.getMessageRoom())
                .senderId(message.getSender().getMemberId())
                .senderNickname(message.getSender().getNickname())
                .senderProfileImageUrl(message.getSender().getProfileImageUrl())
                .receiverId(message.getReceiver().getMemberId())
                .receiverNickname(message.getReceiver().getNickname())
                .receiverProfileImageUrl(message.getReceiver().getProfileImageUrl())
                .build();
    }
}
