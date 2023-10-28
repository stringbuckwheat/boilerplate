package com.memil.setting.repository;

import com.memil.setting.entity.User;

import java.util.Optional;

public interface UserQueryRepository {
    Optional<User> findByUsername(String username);
}
