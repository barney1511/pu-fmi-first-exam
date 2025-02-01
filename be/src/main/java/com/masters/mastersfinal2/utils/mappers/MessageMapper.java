package com.masters.mastersfinal2.utils.mappers;

import com.masters.mastersfinal2.dtos.requests.message.MessageCreateRequest;
import com.masters.mastersfinal2.dtos.requests.message.MessageUpdateRequest;
import com.masters.mastersfinal2.dtos.responses.message.MessageResponse;
import com.masters.mastersfinal2.entities.Message;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface MessageMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "channel", ignore = true)
    @Mapping(target = "sender", ignore = true)
    @Mapping(target = "recipient", ignore = true)
    Message toEntity(MessageCreateRequest request);

    @Mapping(target = "messageId", source = "id")
    @Mapping(target = "channelId", source = "channel.id")
    @Mapping(target = "sender", source = "sender")
    @Mapping(target = "recipient", source = "recipient")
    @Mapping(target = "content", source = "content")
    MessageResponse toResponse(Message message);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateEntity(@MappingTarget Message message, MessageUpdateRequest request);
}
