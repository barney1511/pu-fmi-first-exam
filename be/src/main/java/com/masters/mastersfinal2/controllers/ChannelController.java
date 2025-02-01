package com.masters.mastersfinal2.controllers;

import com.masters.mastersfinal2.dtos.requests.channel.ChannelCreateRequest;
import com.masters.mastersfinal2.dtos.requests.channel.ChannelMemberAddRequest;
import com.masters.mastersfinal2.dtos.requests.channel.ChannelMemberUpdateRequest;
import com.masters.mastersfinal2.dtos.requests.channel.ChannelUpdateRequest;
import com.masters.mastersfinal2.dtos.responses.channel.ChannelResponse;
import com.masters.mastersfinal2.dtos.responses.page.PageResponse;
import com.masters.mastersfinal2.services.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/channels")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;

    @GetMapping
    public ResponseEntity<PageResponse<ChannelResponse>> getAllChannelsByUsername(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam String username) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortBy));

        return ResponseEntity.ok(channelService.getAllChannelsByUsername(username, pageRequest));
    }

    @PostMapping
    public ResponseEntity<ChannelResponse> createChannel(@RequestBody ChannelCreateRequest request) {
        ChannelResponse response = channelService.createChannel(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChannelResponse> getChannel(@PathVariable UUID id) {
        ChannelResponse response = channelService.getChannel(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID id) {
        channelService.deleteChannel(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<ChannelResponse> updateChannel(@RequestBody ChannelUpdateRequest request) {
        ChannelResponse response = channelService.updateChannel(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<ChannelResponse> addMember(@PathVariable UUID id, @RequestBody ChannelMemberAddRequest request) {
        ChannelResponse response = channelService.addMember(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/members/{username}")
    public ResponseEntity<Void> removeMember(@PathVariable UUID id, @PathVariable String username) {
        channelService.removeMember(id, username);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/members/{username}")
    public ResponseEntity<ChannelResponse> updateMemberRole(@PathVariable UUID id, @PathVariable String username, @RequestBody ChannelMemberUpdateRequest request) {
        ChannelResponse response = channelService.changeMemberRole(id, username, request);
        return ResponseEntity.ok(response);
    }
}
