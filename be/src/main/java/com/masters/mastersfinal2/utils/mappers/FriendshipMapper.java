package com.masters.mastersfinal2.utils.mappers;

import com.masters.mastersfinal2.dtos.requests.friendship.FriendshipCreateRequest;
import com.masters.mastersfinal2.dtos.requests.friendship.FriendshipUpdateRequest;
import com.masters.mastersfinal2.dtos.responses.friendship.FriendshipResponse;
import com.masters.mastersfinal2.entities.Friendship;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface FriendshipMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "friend", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    Friendship toEntity(FriendshipCreateRequest request);

    @Mapping(target = "friendshipId", source = "id")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "friendId", source = "friend.id")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "friendName", source = "friend.username")
    FriendshipResponse toResponse(Friendship friendship);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateEntity(@MappingTarget Friendship friendship, FriendshipUpdateRequest request);
}
