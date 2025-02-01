package com.masters.mastersfinal2.entities;

import com.masters.mastersfinal2.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "messages")
@NoArgsConstructor
@Data
public class Message extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User recipient;

    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType type;

    public enum MessageType {
        CHANNEL,
        DIRECT
    }
}
