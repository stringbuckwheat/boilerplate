package com.memil.setting.user.dto;

import com.memil.setting.common.enums.Provider;
import com.memil.setting.user.model.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserResponse {
    private Long id;
    private String username;
    private String name;
    private Provider provider;
    private LocalDateTime createdAt;

    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.provider = user.getProvider();
        this.createdAt = user.getCreatedAt();
    }
}
