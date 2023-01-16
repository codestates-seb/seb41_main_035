package com.lookatme.server.message.service;

import com.lookatme.server.auth.dto.MemberPrincipal;
import com.lookatme.server.exception.ErrorCode;
import com.lookatme.server.exception.ErrorLogicException;
import com.lookatme.server.exception.message.MessageNotFoundException;
import com.lookatme.server.exception.message.MessageNotSendToSelfException;
import com.lookatme.server.member.entity.Member;
import com.lookatme.server.member.repository.MemberRepository;
import com.lookatme.server.message.dto.MessagePostDto;
import com.lookatme.server.message.entity.Message;
import com.lookatme.server.message.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Page<Message> getReceivedMessage(final MemberPrincipal memberPrincipal, final int page, final int size) {
        //받은 편지함 불러오기, 한 명의 유저가 받은 모든 메시지;
        return messageRepository.findAllMessagesByReceiver(memberPrincipal.getMemberId(),
                PageRequest.of(page, size, Sort.by("createdAt")));
    }

    @Transactional(readOnly = true)
    public Page<Message> getSentMessage(final MemberPrincipal memberPrincipal, final int page, final int size) {
        //보낸 편지함 불러오기, 한 명의 유저가 받은 모든 메시지
        return messageRepository.findAllMessagesBySender(memberPrincipal.getMemberId(),
                PageRequest.of(page, size, Sort.by("createdAt")));
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
