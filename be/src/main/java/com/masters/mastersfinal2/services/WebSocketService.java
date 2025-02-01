package com.masters.mastersfinal2.services;

import com.masters.mastersfinal2.dtos.responses.message.MessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebSocketService {
    private final SimpMessagingTemplate messagingTemplate;
    private static final String CHANNEL_DESTINATION = "/topic/channel/";
    private static final String DIRECT_MESSAGE_DESTINATION = "/queue/messages";

    public void sendChannelMessage(UUID channelId, MessageResponse message) {
        String destination = CHANNEL_DESTINATION + channelId;
        messagingTemplate.convertAndSend(destination, message);
        log.info("Message sent to channel {}: {}", channelId, message.getMessageId());
    }

    public void sendDirectMessage(String username, MessageResponse message) {
        messagingTemplate.convertAndSendToUser(username, DIRECT_MESSAGE_DESTINATION, message);
        log.info("Direct message sent to user {}: {}", username, message.getMessageId());
    }
}
