package com.masters.mastersfinal2.dtos.responses.channel;

import lombok.Data;

import java.util.UUID;

@Data
public class ChannelMemberResponse {
    private UUID userId;
    private UUID channelId;
    private String username;
    private String channelName;
    private String role;
}
