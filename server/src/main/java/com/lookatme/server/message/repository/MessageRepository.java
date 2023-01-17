package com.lookatme.server.message.repository;

import com.lookatme.server.message.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("select m from Message m where (sender_id = :sender_id and receiver_id = :receiver_id) or (sender_id = :receiver_id and receiver_id = :sender_id)")
    Page<Message> findAllMessages(@Param("sender_id") Long senderId, @Param("receiver_id") Long receiverId, Pageable pageable);
//    @Query("select m from Message m where sender_id = :id")
//    Page<Message> findAllMessagesBySender(@Param("id") Long senderId, Pageable pageable);
}
