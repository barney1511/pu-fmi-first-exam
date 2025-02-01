package com.masters.mastersfinal2.dtos.requests.message;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class MessageUpdateRequest {

    @NotNull(message = "Message ID is required.")
    private UUID messageId;

    @Size(max = 4000)
    private String content;
}
