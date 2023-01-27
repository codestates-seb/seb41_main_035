package com.lookatme.server.message.entity;

import com.lookatme.server.audit.BaseTimeEntity;
import com.lookatme.server.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "message")
public class Message extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long messageId;

    @Column(name = "content", nullable = false)
    private String content;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "deleted_by_sender", nullable = false)
    private boolean deletedBySender = false;

    @Column(name = "deleted_by_receiver", nullable = false)
    private boolean deletedByReceiver = false;

    @Column(name = "message_room", nullable = false)
    private Long messageRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Member receiver;

    private Message() {

    }

    @Builder
    public Message(final Long messageId,
                   final String content,
                   final LocalDateTime createdAt,
                   final boolean deletedBySender,
                   final boolean deletedByReceiver,
                   final Long messageRoom,
                   final Member sender,
                   final Member receiver
                   ) {
        this.messageId = messageId;
        this.content = content;
        this.createdDate = createdAt;
        this.deletedBySender = deletedBySender;
        this.deletedByReceiver = deletedByReceiver;
        this.messageRoom = messageRoom;
        this.sender = sender;
        this.receiver = receiver;
    }
    public void addSender(final Member sender) {
        this.sender = sender;
    }

    public void addReceiver(final Member receiver) {
        this.receiver = receiver;
    }

    public void setMessageRoom(final Long messageRoom) {
        this.messageRoom = messageRoom;
    }

    public void deleteBySender() {
        this.deletedBySender = true;
    }

    public void deleteByReceiver() {
        this.deletedByReceiver = true;
    }

    public boolean isDeleted() {
        return isDeletedBySender() && isDeletedByReceiver();
    }
}
