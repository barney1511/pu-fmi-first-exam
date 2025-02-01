package com.masters.mastersfinal2.dtos.requests.message;

import com.masters.mastersfinal2.entities.Message;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class MessageCreateRequest {

    private UUID channelId;

    private UUID recipientId;

    private UUID senderId;

    private Message.MessageType type;

    @NotBlank(message = "Message must not be empty.")
    @Size(max = 4000)
    private String content;
}
