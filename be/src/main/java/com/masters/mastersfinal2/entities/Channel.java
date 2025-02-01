package com.masters.mastersfinal2.entities;

import com.masters.mastersfinal2.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "channels")
@Data
public class Channel extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "channel")
    @OrderBy("createdAt asc")
    private Set<ChannelMember> members;

    @OneToMany(mappedBy = "channel")
    private List<Message> messages;
}
