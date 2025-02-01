package com.masters.mastersfinal2.dtos.requests.channel;

import com.masters.mastersfinal2.entities.ChannelMember;
import lombok.Data;

@Data
public class ChannelMemberUpdateRequest {
    private ChannelMember.ChannelRole role;
}
