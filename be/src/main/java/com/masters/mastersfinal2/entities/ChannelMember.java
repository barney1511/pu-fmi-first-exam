package com.masters.mastersfinal2.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.masters.mastersfinal2.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true, exclude = {"user", "channel"})
@Entity
@Table(name = "channel_members",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "channel_id"}))
@Data
public class ChannelMember extends BaseEntity {

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChannelRole role = ChannelRole.MEMBER;

    public enum ChannelRole {
        OWNER,
        ADMINISTRATOR,
        MEMBER
    }
}
