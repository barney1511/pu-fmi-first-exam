package com.masters.mastersfinal2.services;

import com.masters.mastersfinal2.dtos.requests.friendship.FriendshipCreateRequest;
import com.masters.mastersfinal2.dtos.requests.friendship.FriendshipUpdateRequest;
import com.masters.mastersfinal2.dtos.responses.friendship.FriendshipResponse;
import com.masters.mastersfinal2.dtos.responses.page.PageResponse;
import com.masters.mastersfinal2.entities.Friendship;
import com.masters.mastersfinal2.entities.User;
import com.masters.mastersfinal2.repos.FriendshipRepo;
import com.masters.mastersfinal2.repos.UserRepo;
import com.masters.mastersfinal2.utils.mappers.FriendshipMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendshipService {
    private final FriendshipRepo friendshipRepository;
    private final FriendshipMapper friendshipMapper;
    private final UserRepo userRepo;

    public FriendshipResponse getFriendship(UUID id) {
        Optional<Friendship> friendship = friendshipRepository.findById(id);
        if (friendship.isEmpty()) {
            throw new RuntimeException("Friendship not found.");
        }
        return friendshipMapper.toResponse(friendship.get());
    }

    public PageResponse<FriendshipResponse> getAllFriendshipsByUsername(String username, PageRequest pageRequest) {
        Optional<User> existingUser = userRepo.findByUsername(username);
        if (existingUser.isEmpty()) {
            throw new RuntimeException("User not found.");
        }
        UUID userId = existingUser.get().getId();
        Page<Friendship> friendshipsPage = friendshipRepository.findAllByUserId(userId, pageRequest);
        List<FriendshipResponse> friendships = friendshipsPage.getContent()
                .stream()
                .map(friendshipMapper::toResponse)
                .toList();
        return PageResponse.<FriendshipResponse>builder()
                .content(friendships)
                .currentPage(friendshipsPage.getNumber())
                .totalPages(friendshipsPage.getTotalPages())
                .totalElements(friendshipsPage.getTotalElements())
                .size(friendshipsPage.getSize())
                .hasNext(friendshipsPage.hasNext())
                .hasPrevious(friendshipsPage.hasPrevious())
                .build();
    }

    public FriendshipResponse createFriendship(FriendshipCreateRequest request) {
        User user = userRepo.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        User friend = userRepo.findById(request.getFriendId())
                .orElseThrow(() -> new IllegalArgumentException("Friend not found"));

        Optional<Friendship> existingFriendship = friendshipRepository.findFriendship(request.getUserId(), request.getFriendId());
        if (existingFriendship.isEmpty()) {
            existingFriendship = friendshipRepository.findFriendship(request.getFriendId(), request.getUserId());
        }
        if (existingFriendship.isPresent()) {
            Friendship friendship = existingFriendship.get();
            if (friendship.getStatus() == Friendship.FriendshipStatus.DECLINED) {
                friendship.setStatus(Friendship.FriendshipStatus.PENDING);
                friendshipRepository.save(friendship);
            }
            return friendshipMapper.toResponse(friendship);
        }

        Friendship friendship = friendshipMapper.toEntity(request);
        friendship.setUser(user);
        friendship.setFriend(friend);

        Friendship savedFriendship = friendshipRepository.save(friendship);
        return friendshipMapper.toResponse(savedFriendship);
    }

    public FriendshipResponse updateFriendshipStatus(FriendshipUpdateRequest request) {
        Optional<Friendship> friendship = friendshipRepository.findFriendship(request.getUserId(), request.getFriendId());
        if (friendship.isEmpty()) {
            throw new RuntimeException("Friendship not found.");
        }
        friendshipMapper.updateEntity(friendship.get(), request);
        return friendshipMapper.toResponse(friendship.get());
    }
}
