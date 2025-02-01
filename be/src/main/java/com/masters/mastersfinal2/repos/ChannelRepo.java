package com.masters.mastersfinal2.repos;

import com.masters.mastersfinal2.entities.Channel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ChannelRepo extends JpaRepository<Channel, UUID> {

    @Query("""
           SELECT c FROM Channel c
           LEFT JOIN FETCH c.members cm
           WHERE c.id = :id
           """)
    Optional<Channel> findByIdWithMembers(@Param("id") UUID id);

    @Query("""
           SELECT DISTINCT c FROM Channel c
           LEFT JOIN FETCH c.members cm
           WHERE EXISTS (SELECT 1 FROM ChannelMember m
                         WHERE m.channel = c
                         AND m.user.id = :userId)
           """)
    Page<Channel> findAccessibleChannels(@Param("userId") UUID userId, Pageable pageable);

//    @Query(value = """
//            SELECT c.* FROM channels c
//            WHERE c.deleted = false
//            AND (LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
//           """, nativeQuery = true)
//    Page<Channel> searchChannels(@Param("searchTerm") String searchTerm, Pageable pageable);
}
