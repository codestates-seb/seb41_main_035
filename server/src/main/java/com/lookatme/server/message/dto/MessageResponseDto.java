package com.lookatme.server.message.dto;

import com.lookatme.server.member.entity.Member;
import com.lookatme.server.message.entity.Message;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MessageResponseDto {
    private Long messageId;
    private String content;
    private LocalDateTime createdAt;
    private String senderNickname;
    private String senderProfileImageUrl;
    private String receiverNickname;
    private String receiverProfileImageUrl;

    @Builder
    public MessageResponseDto(final Long messageId,
                              final String content,
                              final LocalDateTime createdAt,
                              final String senderNickname,
                              final String senderProfileImageUrl,
                              final String receiverNickname,
                              final String receiverProfileImageUrl) {
        this.messageId = messageId;
        this.content = content;
        this.createdAt = createdAt;
        this.senderNickname = senderNickname;
        this.senderProfileImageUrl = senderProfileImageUrl;
        this.receiverNickname = receiverNickname;
        this.receiverProfileImageUrl = receiverProfileImageUrl;
    }

    public static MessageResponseDto of(final Message message) {
        return MessageResponseDto.builder()
                .messageId(message.getMessageId())
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .senderNickname(message.getSender().getNickname())
                .senderProfileImageUrl(message.getSender().getProfileImageUrl())
                .receiverNickname(message.getReceiver().getNickname())
                .receiverProfileImageUrl(message.getReceiver().getProfileImageUrl())
                .build();
    }
}
