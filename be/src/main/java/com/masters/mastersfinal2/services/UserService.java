package com.masters.mastersfinal2.services;

import com.masters.mastersfinal2.dtos.requests.user.UserCreateRequest;
import com.masters.mastersfinal2.dtos.requests.user.UserUpdateRequest;
import com.masters.mastersfinal2.dtos.responses.page.PageResponse;
import com.masters.mastersfinal2.dtos.responses.user.UserResponse;
import com.masters.mastersfinal2.entities.User;
import com.masters.mastersfinal2.repos.UserRepo;
import com.masters.mastersfinal2.utils.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserMapper userMapper;
    private final UserRepo userRepo;

    public UserResponse getUserById(UUID id) {
        return userMapper.toResponse(userRepo.getReferenceById(id));
    }

    public PageResponse<UserResponse> searchUsersbyName(String name, PageRequest pageRequest) {
        Page<User> users = userRepo.searchUsers(name, pageRequest);
        return PageResponse.<UserResponse>builder()
                .content(users.map(userMapper::toResponse).toList())
                .currentPage(users.getNumber())
                .totalPages(users.getTotalPages())
                .totalElements(users.getTotalElements())
                .size(users.getSize())
                .hasNext(users.hasNext())
                .hasPrevious(users.hasPrevious())
                .build();
    }

    public UserResponse createUser(UserCreateRequest createRequest) {
    User user = userMapper.toEntity(createRequest);
    return userMapper.toResponse(userRepo.save(user));
}

    public UserResponse updateUser(UserUpdateRequest updateRequest) {
        User user = userRepo.getReferenceById(updateRequest.getId());
        userMapper.updateEntity(user, updateRequest);
        return userMapper.toResponse(user);
    }

    public void deleteUser(UUID id) {
        userRepo.deleteById(id);
    }
}
