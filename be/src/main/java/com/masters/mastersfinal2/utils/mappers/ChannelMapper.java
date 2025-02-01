package com.masters.mastersfinal2.utils.mappers;

import com.masters.mastersfinal2.dtos.requests.channel.ChannelCreateRequest;
import com.masters.mastersfinal2.dtos.requests.channel.ChannelUpdateRequest;
import com.masters.mastersfinal2.dtos.responses.channel.ChannelMemberResponse;
import com.masters.mastersfinal2.dtos.responses.channel.ChannelResponse;
import com.masters.mastersfinal2.entities.Channel;
import com.masters.mastersfinal2.entities.ChannelMember;
import jakarta.persistence.OrderBy;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface ChannelMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "members")
    Channel toEntity(ChannelCreateRequest request);

    @Mapping(target = "channelId", source = "id")
    @Mapping(target = "members", source = "members")
    ChannelResponse toResponse(Channel channel);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "role", source = "role")
    @Mapping(target = "channelId", source = "channel.id")
    @Mapping(target = "channelName", source = "channel.name")
    ChannelMemberResponse toMemberResponse(ChannelMember member);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "members", ignore = true)
    void updateEntity(@MappingTarget Channel channel, ChannelUpdateRequest request);
}
