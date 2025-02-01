package com.masters.mastersfinal2.utils.mappers;

import com.masters.mastersfinal2.dtos.requests.user.UserCreateRequest;
import com.masters.mastersfinal2.dtos.requests.user.UserUpdateRequest;
import com.masters.mastersfinal2.dtos.responses.user.UserResponse;
import com.masters.mastersfinal2.entities.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    User toEntity(UserCreateRequest request);

    @Mapping(target = "userId", source = "id")
    UserResponse toResponse(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateEntity(@MappingTarget User user, UserUpdateRequest request);
}
