package com.masters.mastersfinal2.controllers;

import com.masters.mastersfinal2.dtos.requests.friendship.FriendshipCreateRequest;
import com.masters.mastersfinal2.dtos.requests.friendship.FriendshipUpdateRequest;
import com.masters.mastersfinal2.dtos.responses.friendship.FriendshipResponse;
import com.masters.mastersfinal2.dtos.responses.page.PageResponse;
import com.masters.mastersfinal2.services.FriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/friendships")
@RequiredArgsConstructor
public class FriendshipController {
    private final FriendshipService friendshipService;

    @GetMapping("/{id}")
    public ResponseEntity<FriendshipResponse> getFriendship(@PathVariable UUID id) {
        FriendshipResponse response = friendshipService.getFriendship(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<PageResponse<FriendshipResponse>> getFriends(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam String username) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sortBy));

        return ResponseEntity.ok(friendshipService.getAllFriendshipsByUsername(username, pageRequest));
    }

    @PostMapping
    public ResponseEntity<FriendshipResponse> createFriendship(@RequestBody FriendshipCreateRequest request) {
        FriendshipResponse response = friendshipService.createFriendship(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<FriendshipResponse> updateFriendship(@RequestBody FriendshipUpdateRequest request) {
        FriendshipResponse response = friendshipService.updateFriendshipStatus(request);
        return ResponseEntity.ok(response);
    }
}
