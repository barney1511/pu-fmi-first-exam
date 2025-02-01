package com.masters.mastersfinal2.controllers;

import com.masters.mastersfinal2.dtos.requests.message.MessageCreateRequest;
import com.masters.mastersfinal2.dtos.responses.message.MessageResponse;
import com.masters.mastersfinal2.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final MessageService messageService;

    @MessageMapping("/channel/{channelId}")
    @SendTo("/topic/channel/{channelId}")
    public void sendChannelMessage(@DestinationVariable UUID channelId, @Payload MessageCreateRequest request) {
        messageService.sendChannelMessage(channelId, request);
    }

    @MessageMapping("/direct/{recipientId}")
    @SendTo("/topic/direct")
    public void sendDirectMessage(@DestinationVariable UUID recipientId, @Payload MessageCreateRequest request) {
        messageService.sendDirectMessage(recipientId, request);
    }
}
