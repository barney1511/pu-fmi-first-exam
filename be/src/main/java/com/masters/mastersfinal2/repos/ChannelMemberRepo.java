package com.masters.mastersfinal2.repos;

import com.masters.mastersfinal2.entities.Channel;
import com.masters.mastersfinal2.entities.ChannelMember;
import com.masters.mastersfinal2.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ChannelMemberRepo extends JpaRepository<ChannelMember, UUID> {

    @Query("SELECT cm FROM ChannelMember cm WHERE cm.channel = :channel AND cm.user = :user")
    Optional<ChannelMember> findByChannelAndUser(@Param("channel") Channel channel, @Param("user") User user);

    @Query(value = "SELECT * FROM channel_members cm WHERE cm.channel_id = :channelId AND cm.user_id = :userId", nativeQuery = true)
    Optional<ChannelMember> findByChannelAndUserWithDeleted(@Param("channelId") UUID channelId, @Param("userId") UUID userId);

    @Modifying
    @Query(value = "UPDATE channel_members SET deleted = false WHERE user_id = :userId AND channel_id = :channelId", nativeQuery = true)
    void undeleteChannelMember(@Param("userId") UUID userId, @Param("channelId") UUID channelId);
}
