package com.lookatme.server.message.service;

import com.lookatme.server.auth.dto.MemberPrincipal;
import com.lookatme.server.common.dto.MultiResponseDto;
import com.lookatme.server.exception.ErrorCode;
import com.lookatme.server.exception.ErrorLogicException;
import com.lookatme.server.exception.message.MessageNotFoundException;
import com.lookatme.server.exception.message.MessageNotSendToSelfException;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.repository.MemberRepository;
import com.lookatme.server.message.dto.MessagePostDto;
import com.lookatme.server.message.dto.MessageResponseDto;
import com.lookatme.server.message.entity.Message;
import com.lookatme.server.message.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Message createMessage(final MessagePostDto messagePostDto,
                                 final MemberPrincipal memberPrincipal) {
        Member sender = findValidateMember(memberPrincipal.getMemberId());

        Message message = messagePostDto.toMessage();
        message.addSender(sender);

        Member receiver = findValidateMember(messagePostDto.getReceiverNickname());
        message.addReceiver(receiver);

        checkMyself(sender.getMemberId(), receiver.getMemberId());

        return messageRepository.save(message);
    }

    private void checkMyself(final Long senderId, final Long receiverId) {
        if (senderId.equals(receiverId)) {
            throw new MessageNotSendToSelfException();
        }
    }

    private Member findValidateMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ErrorLogicException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private Member findValidateMember(final String nickname) {
        return memberRepository.findByNickname(nickname)
                .orElseThrow(() -> new ErrorLogicException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Message findMessageById(Long messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new MessageNotFoundException());
    }

    @Transactional(readOnly = true)
    public Message getMessage(Long messageId) {
        return findMessageById(messageId);
    }

    private void checkValidateMember(final String authMemberEmail,
                                     final String messageMemberEmail) {
        if (!authMemberEmail.equals(messageMemberEmail)) {
            throw new ErrorLogicException(ErrorCode.UNAUTHORIZED);
        }
    }

    @Transactional(readOnly = true)
    public MultiResponseDto getMessages(final MemberPrincipal memberPrincipal,
                                     final Long memberId,
                                     final int page, final int size) {
        //특정한 사람과 주고 받은 모든 메시지 조회
        Page<Message> messages = messageRepository.findAllMessages(memberPrincipal.getMemberId(), memberId,
                PageRequest.of(page, size, Sort.by("createdAt")));
        List<MessageResponseDto> messageResponseDtos = messages.getContent()
                .stream()
                .map(message -> MessageResponseDto.of(message))
                .collect(Collectors.toList());
        return new MultiResponseDto<>(messageResponseDtos, messages);
    }

    //받은 편지 삭제
    @Transactional
    public void deleteMessageByReceiver(final Long messageId, final MemberPrincipal memberPrincipal) {
        Message message = findMessageById(messageId);
        checkValidateMember(memberPrincipal.getEmail(), message.getReceiver().getEmail());
        message.deleteByReceiver();
        if (message.isDeleted()) {
            messageRepository.delete(message);
        }
    }

    //보낸 편지 삭제
    @Transactional
    public void deleteMessageBySender(final Long messageId, final MemberPrincipal memberPrincipal) {
        Message message = findMessageById(messageId);
        checkValidateMember(memberPrincipal.getEmail(), message.getSender().getEmail());
        message.deleteBySender();
        if (message.isDeleted()) {
            messageRepository.delete(message);
        }
    }
}
