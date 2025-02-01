package com.masters.mastersfinal2.dtos.responses.channel;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class ChannelResponse {
    private UUID channelId;
    private String name;
    private Set<ChannelMemberResponse> members;
}
