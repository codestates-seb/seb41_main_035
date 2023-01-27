package com.lookatme.server.message.repository;

import com.lookatme.server.message.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("select m from Message m where (sender_id = :sender_id and receiver_id = :receiver_id) or (sender_id = :receiver_id and receiver_id = :sender_id)")
    Page<Message> findAllMessages(@Param("sender_id") Long senderId, @Param("receiver_id") Long receiverId, Pageable pageable);

    @Query(nativeQuery = true,
            value = "select * from message where (sender_id = :sender_id and receiver_id = :receiver_id) or (sender_id = :receiver_id and receiver_id = :sender_id) limit 1")
    Message findExistedMessageRoom(@Param("sender_id") Long senderId, @Param("receiver_id") Long receiverId);

    Message findTopByOrderByMessageRoomDesc();

    @Query("select m from Message m group by message_room, message_id order by created_at desc")
    List<Message> findMessageRoomList();
    //"select distinct(message_room) * from (select * from message where sender_id = :member_id or receiver_id = :member_id order by created_at desc) as m"
}
