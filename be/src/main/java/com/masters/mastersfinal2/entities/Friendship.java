package com.masters.mastersfinal2.entities;

import com.masters.mastersfinal2.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "friendships", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "friend_id"}),
        @UniqueConstraint(columnNames = {"friend_id", "user_id"})
    })
@NoArgsConstructor
@Data
public class Friendship extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "friend_id", nullable = false)
    private User friend;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FriendshipStatus status = FriendshipStatus.PENDING;

    public enum FriendshipStatus {
        PENDING,
        ACCEPTED,
        DECLINED
    }
}
