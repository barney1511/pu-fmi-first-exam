package com.masters.mastersfinal2.repos;

import com.masters.mastersfinal2.entities.Friendship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface FriendshipRepo extends JpaRepository<Friendship, UUID> {

    @Query("""
           SELECT f FROM Friendship f
           WHERE (f.user.id = :userId AND f.friend.id = :friendId)
           """)
    Optional<Friendship> findFriendship(@Param("userId") UUID userId, @Param("friendId") UUID friendId);

    @Query("""
           SELECT f FROM Friendship f
           WHERE (f.user.id = :userId OR f.friend.id = :userId) AND f.status != 'DECLINED'
           """)
    Page<Friendship> findAllByUserId(@Param("userId") UUID userId, Pageable pageable);

//
//    @Query("""
//           SELECT f FROM Friendship f
//           WHERE (f.user.id = :userId OR f.friend.id = :userId)
//           """)
//    Page<Friendship> findFriendshipsByUserId(@Param("userId") UUID userId, Pageable pageable);
//
//    @Query("""
//           SELECT f.status FROM Friendship f
//           WHERE (f.user.id = :userId AND f.friend.id = :friendId)
//           """)
//    Optional<Friendship> findFriendshipStatus(@Param("userId") UUID userId, @Param("friendId") UUID friendId);
}
