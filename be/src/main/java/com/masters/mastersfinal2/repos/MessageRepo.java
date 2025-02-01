package com.masters.mastersfinal2.repos;

import com.masters.mastersfinal2.entities.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface MessageRepo extends JpaRepository<Message, UUID> {

    @Query("""
           SELECT m FROM Message m
           WHERE m.channel.id = :channelId
           ORDER BY m.createdAt ASC
           """)
    Page<Message> findByChannelId(@Param("channelId") UUID channelId, Pageable pageable);

    @Query("""
           SELECT m FROM Message m
           WHERE m.sender.id = :senderId
           AND m.recipient.id = :recipientId
           ORDER BY m.createdAt ASC
           """)
    Page<Message> findDirectMessagesBetweenUsers(@Param("senderId") UUID senderId, @Param("recipientId") UUID recipientId, Pageable pageable);

//    @Query("""
//           SELECT m FROM Message m
//           WHERE m.channel.id = :channelId
//           AND m.deleted = false
//           ORDER BY m.createdAt DESC
//           """)
//    Page<Message> findChannelMessages(@Param("channelId") UUID channelId, Pageable pageable);
//
//    @Query("""
//           SELECT m FROM Message m
//           WHERE m.sender.id = :senderId
//           AND m.recipient.id = :recipientId
//           AND m.deleted = false
//           ORDER BY m.createdAt DESC
//           """)
//    Page<Message> findDirectMessagesBetweenUsers(@Param("senderId") UUID senderId,@Param("recipientId") UUID recipientId, Pageable pageable);
//
//    @Query(value = """
//           SELECT m.* FROM messages m
//           WHERE m.channel_id = :channelId
//           AND m.deleted = false
//           AND to_tsvector('english', m.content) @@ plainto_tsquery('english', :searchTerm)
//           ORDER BY ts_rank(to_tsvector('english', m.content),
//                            plainto_tsquery('english', :searchTerm)) DESC
//           """, nativeQuery = true)
//    Page<Message> searchMessages(@Param("channelId") UUID channelId, @Param("searchTerm") String searchTerm, Pageable pageable);
}
