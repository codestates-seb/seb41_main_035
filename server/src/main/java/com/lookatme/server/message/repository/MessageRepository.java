package com.lookatme.server.message.repository;

import com.lookatme.server.message.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("select m from Message m where receiver_id = :id")
    Page<Message> findAllMessagesByReceiver(@Param("id") Long receiverId, Pageable pageable);

    @Query("select m from Message m where sender_id = :id")
    Page<Message> findAllMessagesBySender(@Param("id") Long senderId, Pageable pageable);
}
