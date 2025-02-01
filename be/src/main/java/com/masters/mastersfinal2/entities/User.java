package com.masters.mastersfinal2.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.masters.mastersfinal2.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
@NoArgsConstructor
@Data
public class User extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private Set<ChannelMember> channelMemberships;

    @OneToMany(mappedBy = "sender")
    private List<Message> sentMessages;
}
