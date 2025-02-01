package com.masters.mastersfinal2.dtos.requests.user;

import lombok.Data;
import jakarta.validation.constraints.Pattern;

@Data
public class UserCreateRequest  {

    @Pattern(regexp = "^[a-zA-Z0-9._-]{3,50}$", message = "Username must be between 3 and 50 characters and contain only letters, numbers, dots, underscores, and hyphens.")
    private String username;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$", message = "Password must be at least 8 characters and contain at least one uppercase letter, one lowercase letter, and one number.")
    private String password;
}
