package com.masters.mastersfinal2.dtos.responses.friendship;

import com.masters.mastersfinal2.entities.Friendship;
import lombok.Data;

import java.util.UUID;

@Data
public class FriendshipResponse {
    private UUID friendshipId;
    private UUID userId;
    private UUID friendId;
    private String username;
    private String friendName;
    private Friendship.FriendshipStatus status;
}
