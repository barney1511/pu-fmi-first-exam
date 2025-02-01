package com.masters.mastersfinal2.dtos.responses.user;

import lombok.Data;

import java.util.UUID;

@Data
public class UserResponse {
    private UUID userId;
    private String username;
}
