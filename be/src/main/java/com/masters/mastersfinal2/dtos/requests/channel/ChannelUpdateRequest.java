package com.masters.mastersfinal2.dtos.requests.channel;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.UUID;

@Data
public class ChannelUpdateRequest {

    @NotNull(message = "Channel ID is required.")
    private UUID id;

    @Pattern(regexp = "^[a-zA-Z0-9._-]{3,50}$", message = "Channel name must be between 3 and 50 characters and contain only letters, numbers, dots, underscores, and hyphens.")
    private String name;
}
