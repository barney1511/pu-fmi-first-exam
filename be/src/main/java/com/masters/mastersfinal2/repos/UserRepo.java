package com.masters.mastersfinal2.repos;

import com.masters.mastersfinal2.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {

    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);
//
//    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u " +
//            "WHERE u.username = :username AND u.deleted = false")
//    boolean existsByUsername(@Param("username") String username);
//
    @Query("SELECT u FROM User u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<User> searchUsers(@Param("searchTerm") String searchTerm, Pageable pageable);
//
//    @Query("""
//           SELECT u FROM User u
//           JOIN ChannelMember cm ON cm.user = u
//           WHERE cm.channel.id = :channelId
//           AND u.deleted = false
//           AND cm.deleted = false
//           """)
//    Set<User> findChannelMembers(@Param("channelId") UUID channelId);
//
//    @Query("""
//           SELECT u FROM User u
//           JOIN Friendship f ON (f.user = u OR f.friend = u)
//           WHERE (f.user.id = :userId OR f.friend.id = :userId)
//           AND f.status = 'ACCEPTED'
//           AND u.id != :userId
//           AND u.deleted = false
//           AND f.deleted = false
//           """)
//    Set<User> findFriends(@Param("userId") UUID userId);
}
