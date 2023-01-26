package com.lookatme.server.message.controller;

import com.lookatme.server.auth.dto.MemberPrincipal;
import com.lookatme.server.common.dto.MultiResponseDto;
import com.lookatme.server.message.dto.MessagePostDto;
import com.lookatme.server.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageController {
    private final MessageService messageService;

    @PostMapping
    public ResponseEntity sendMessage(@RequestBody @Valid MessagePostDto messagePostDto,
                                      @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        return new ResponseEntity<>(messageService.createMessage(messagePostDto, memberPrincipal), HttpStatus.CREATED);
    }

    @GetMapping("/{messageId}")
    public ResponseEntity getMessage(@PathVariable("messageId") final Long messageId) {
        return new ResponseEntity<>(messageService.getMessage(messageId), HttpStatus.OK);
    }

    @GetMapping("/received/{memberId}")
    public ResponseEntity<MultiResponseDto> getMessages(@AuthenticationPrincipal MemberPrincipal memberPrincipal,
                                                        @PathVariable("memberId") final Long memberId,
                                                        @RequestParam("page") final int page,
                                                        @RequestParam("size") final int size) {
        return new ResponseEntity<>(messageService.getMessages(memberPrincipal, memberId, page - 1, size), HttpStatus.OK);
    }

    @GetMapping("/room")
    public ResponseEntity getMessageRoom(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        return new ResponseEntity<>(messageService.getMessageRoomList(memberPrincipal), HttpStatus.OK);
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
