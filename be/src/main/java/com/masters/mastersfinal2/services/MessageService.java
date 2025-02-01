package com.masters.mastersfinal2.services;

import com.masters.mastersfinal2.dtos.requests.message.MessageCreateRequest;
import com.masters.mastersfinal2.dtos.responses.message.MessageResponse;
import com.masters.mastersfinal2.dtos.responses.page.PageResponse;
import com.masters.mastersfinal2.entities.Message;
import com.masters.mastersfinal2.repos.ChannelRepo;
import com.masters.mastersfinal2.repos.MessageRepo;
import com.masters.mastersfinal2.repos.UserRepo;
import com.masters.mastersfinal2.utils.mappers.MessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {
    private final WebSocketService webSocketService;
    private final MessageRepo messageRepository;
    private final ChannelRepo channelRepo;
    private final MessageMapper messageMapper;
    private final UserRepo userRepository;

    public void sendChannelMessage(UUID channelId, MessageCreateRequest request) {
        Message message = messageMapper.toEntity(request);
        message.setType(Message.MessageType.CHANNEL);
        message.setChannel(channelRepo.getReferenceById(channelId));
        message.setSender(userRepository.getReferenceById(request.getSenderId()));

        Message savedMessage = messageRepository.save(message);
        MessageResponse response = messageMapper.toResponse(savedMessage);

        webSocketService.sendChannelMessage(request.getChannelId(), response);
    }

    public PageResponse<MessageResponse> getChannelMessages(UUID channelId, PageRequest pageRequest) {
        Page<Message> messages = messageRepository.findByChannelId(channelId, pageRequest);
        return PageResponse.<MessageResponse>builder()
                .content(messages.map(messageMapper::toResponse).toList())
                .currentPage(messages.getNumber())
                .totalPages(messages.getTotalPages())
                .totalElements(messages.getTotalElements())
                .size(messages.getSize())
                .hasNext(messages.hasNext())
                .hasPrevious(messages.hasPrevious())
                .build();
    }

    public void sendDirectMessage(UUID recipientId, MessageCreateRequest request) {
        Message message = messageMapper.toEntity(request);
        message.setType(Message.MessageType.DIRECT);
        message.setSender(userRepository.getReferenceById(request.getSenderId()));
        message.setRecipient(userRepository.getReferenceById(recipientId));
        String recipientUsername = userRepository.findById(recipientId).get().getUsername();

        Message savedMessage = messageRepository.save(message);
        MessageResponse response = messageMapper.toResponse(savedMessage);

        webSocketService.sendDirectMessage(recipientUsername, response);
    }

    public PageResponse<MessageResponse> getDirectMessages(UUID senderId, UUID recipientId, PageRequest pageRequest) {
        Page<Message> messages = messageRepository.findDirectMessagesBetweenUsers(senderId, recipientId, pageRequest);
        return PageResponse.<MessageResponse>builder()
                .content(messages.map(messageMapper::toResponse).toList())
                .currentPage(messages.getNumber())
                .totalPages(messages.getTotalPages())
                .totalElements(messages.getTotalElements())
                .size(messages.getSize())
                .hasNext(messages.hasNext())
                .hasPrevious(messages.hasPrevious())
                .build();
    }
}
