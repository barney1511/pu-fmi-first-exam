package com.masters.mastersfinal2.services;

import com.masters.mastersfinal2.dtos.requests.channel.ChannelCreateRequest;
import com.masters.mastersfinal2.dtos.requests.channel.ChannelMemberAddRequest;
import com.masters.mastersfinal2.dtos.requests.channel.ChannelMemberUpdateRequest;
import com.masters.mastersfinal2.dtos.requests.channel.ChannelUpdateRequest;
import com.masters.mastersfinal2.dtos.responses.channel.ChannelResponse;
import com.masters.mastersfinal2.dtos.responses.page.PageResponse;
import com.masters.mastersfinal2.entities.Channel;
import com.masters.mastersfinal2.entities.ChannelMember;
import com.masters.mastersfinal2.entities.User;
import com.masters.mastersfinal2.repos.ChannelMemberRepo;
import com.masters.mastersfinal2.repos.ChannelRepo;
import com.masters.mastersfinal2.repos.UserRepo;
import com.masters.mastersfinal2.utils.mappers.ChannelMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ChannelService {
    private final ChannelMapper channelMapper;
    private final ChannelRepo channelRepo;
    private final UserRepo userRepository;
    private final ChannelMemberRepo channelMemberRepo;

    public ChannelResponse getChannel(UUID id) {
        Optional<Channel> channel = channelRepo.findByIdWithMembers(id);
        if (channel.isEmpty()) {
            throw new RuntimeException("Channel not found.");
        }
        return channelMapper.toResponse(channel.get());
    }

    public PageResponse<ChannelResponse> getAllChannelsByUsername(String username, PageRequest pageRequest) {
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isEmpty()) {
            throw new RuntimeException("User not found.");
        }
        UUID userId = existingUser.get().getId();
        Page<Channel> channels = channelRepo.findAccessibleChannels(userId, pageRequest);
        return PageResponse.<ChannelResponse>builder()
                .content(channels.map(channelMapper::toResponse).toList())
                .currentPage(channels.getNumber())
                .totalPages(channels.getTotalPages())
                .totalElements(channels.getTotalElements())
                .size(channels.getSize())
                .hasNext(channels.hasNext())
                .hasPrevious(channels.hasPrevious())
                .build();
    }

    public ChannelResponse createChannel(ChannelCreateRequest createRequest) {
        Channel channel = channelMapper.toEntity(createRequest);
        channel = channelRepo.save(channel);

        ChannelMember owner = new ChannelMember();
        User ownerUser = userRepository.getReferenceById(createRequest.getOwnerId());

        owner.setUser(ownerUser);
        owner.setChannel(channel);
        owner.setRole(ChannelMember.ChannelRole.OWNER);

        channel.getMembers().add(owner);

        channelMemberRepo.save(owner);
        return channelMapper.toResponse(channel);
    }

    public ChannelResponse updateChannel(ChannelUpdateRequest updateRequest) {
        Channel channel = channelRepo.getReferenceById(updateRequest.getId());
        channelMapper.updateEntity(channel, updateRequest);
        return channelMapper.toResponse(channel);
    }

    public void deleteChannel(UUID id) {
        channelRepo.deleteById(id);
    }

    public ChannelResponse addMember(UUID id, ChannelMemberAddRequest addRequest) {
        Channel channel = channelRepo.getReferenceById(id);
        Optional<User> user = userRepository.findByUsername(addRequest.getUsername());
        if (user.isEmpty()) {
            throw new RuntimeException("User not found.");
        }

        Optional<ChannelMember> existingMember = channelMemberRepo.findByChannelAndUserWithDeleted(channel.getId(), user.get().getId());
        if (existingMember.isPresent()) {
            channelMemberRepo.undeleteChannelMember(user.get().getId(), channel.getId());
            return channelMapper.toResponse(channel);
        }
        ChannelMember member = new ChannelMember();
        member.setUser(user.get());
        member.setChannel(channel);

        channel.getMembers().add(member);
        channelMemberRepo.save(member);
        return channelMapper.toResponse(channel);
    }

    public void removeMember(UUID id, String username) {
        Channel channel = channelRepo.getReferenceById(id);
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found.");
        }

        Optional<ChannelMember> member = channelMemberRepo.findByChannelAndUser(channel, user.get());
        if (member.isEmpty()) {
            throw new RuntimeException("User is not a member of the channel.");
        }

        channel.getMembers().remove(member.get());
        channelMemberRepo.delete(member.get());
    }

    public ChannelResponse changeMemberRole(UUID id, String username, ChannelMemberUpdateRequest request) {
        Channel channel = channelRepo.getReferenceById(id);
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new RuntimeException("User not found.");
        }

        Optional<ChannelMember> member = channelMemberRepo.findByChannelAndUser(channel, user.get());
        if (member.isEmpty()) {
            throw new RuntimeException("User is not a member of the channel.");
        }

        member.get().setRole(request.getRole());
        return channelMapper.toResponse(channel);
    }
}
