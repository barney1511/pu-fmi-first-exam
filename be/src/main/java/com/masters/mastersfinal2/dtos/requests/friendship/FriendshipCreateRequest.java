package com.masters.mastersfinal2.dtos.requests.friendship;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class FriendshipCreateRequest {

    @NotNull(message = "User ID is required.")
    private UUID userId;

    @NotNull(message = "Friend ID is required.")
    private UUID friendId;
}
