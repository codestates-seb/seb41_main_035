package com.lookatme.server.message.dto;

import com.lookatme.server.member.entity.Member;
import com.lookatme.server.message.entity.Message;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class MessagePostDto {
    @NotBlank(message = "메시지 내용을 입력하세요.")
    private String content;

    @NotBlank
    private String receiverNickname;

    private MessagePostDto() {

    }

    @Builder
    public MessagePostDto(final String content,
                          final String receiverNickname) {
        this.content = content;
        this.receiverNickname = receiverNickname;
    }

    public Message toMessage() {
        return Message.builder()
                .content(content)
                .receiver(Member.builder().build())
                .build();
    }
}
