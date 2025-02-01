package com.masters.mastersfinal2.dtos.responses.message;

import com.masters.mastersfinal2.dtos.responses.user.UserResponse;
import com.masters.mastersfinal2.entities.Message;
import lombok.Data;

import java.util.UUID;

@Data
public class MessageResponse {
    private UUID messageId;
    private UUID channelId;
    private UserResponse sender;
    private UserResponse recipient;
    private Message.MessageType type;
    private String content;
}
