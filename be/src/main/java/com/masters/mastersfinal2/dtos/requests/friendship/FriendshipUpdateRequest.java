package com.masters.mastersfinal2.dtos.requests.friendship;

import com.masters.mastersfinal2.entities.Friendship;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class FriendshipUpdateRequest {

    @NotNull(message = "User ID is required.")
    private UUID userId;

    @NotNull(message = "Friend ID is required.")
    private UUID friendId;

    @NotNull(message = "Friendship status is required.")
    private Friendship.FriendshipStatus status;
}
