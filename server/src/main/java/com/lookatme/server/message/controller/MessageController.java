package com.lookatme.server.message.controller;

import com.lookatme.server.auth.dto.MemberPrincipal;
import com.lookatme.server.common.dto.MultiResponseDto;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.repository.MemberRepository;
import com.lookatme.server.message.dto.MessagePostDto;
import com.lookatme.server.message.dto.MessageResponseDto;
import com.lookatme.server.message.entity.Message;
import com.lookatme.server.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageController {
    private final MessageService messageService;

    @PostMapping
    public ResponseEntity sendMessage(@RequestBody @Valid MessagePostDto messagePostDto,
                                      @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        return new ResponseEntity<>(MessageResponseDto.of(messageService.createMessage(messagePostDto, memberPrincipal)), HttpStatus.CREATED);
    }

    @GetMapping("/{messageId}")
    public ResponseEntity getMessage(@PathVariable("messageId") final Long messageId) {
        return new ResponseEntity<>(MessageResponseDto.of(messageService.getMessage(messageId)), HttpStatus.OK);
    }

    @GetMapping("/received")
    public ResponseEntity<MultiResponseDto> getReceivedMessage(@AuthenticationPrincipal MemberPrincipal memberPrincipal,
                                             @RequestParam("page") final int page,
                                             @RequestParam("size") final int size) {
        Page<Message> messages = messageService.getReceivedMessage(memberPrincipal, page - 1, size);
        List<MessageResponseDto> messageResponseDtos = messages.getContent()
                .stream()
                .map(message -> MessageResponseDto.of(message))
                .collect(Collectors.toList());
        return new ResponseEntity<>(new MultiResponseDto<>(messageResponseDtos, messages), HttpStatus.OK);
    }

    @GetMapping("/sent")
    public ResponseEntity<MultiResponseDto> getSentMessage(@AuthenticationPrincipal MemberPrincipal memberPrincipal,
                                         @RequestParam("page") final int page,
                                         @RequestParam("size") final int size) {
        Page<Message> messages = messageService.getSentMessage(memberPrincipal, page - 1, size);
        List<MessageResponseDto> messageResponseDtos = messages.getContent()
                .stream()
                .map(message -> MessageResponseDto.of(message))
                .collect(Collectors.toList());
        return new ResponseEntity<>(new MultiResponseDto<>(messageResponseDtos, messages), HttpStatus.OK);
    }

    @DeleteMapping("/received/{messageId}")
    public ResponseEntity deleteReceivedMessage(@PathVariable("messageId") Long messageId,
                                                @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        messageService.deleteMessageByReceiver(messageId, memberPrincipal);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/sent/{messageId}")
    public ResponseEntity deleteSentMessage(@PathVariable("messageId") Long messageId,
                                            @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        messageService.deleteMessageBySender(messageId, memberPrincipal);
        return new ResponseEntity(HttpStatus.OK);
    }
}
