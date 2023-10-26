package com.memil.setting.repository;

import com.memil.setting.entity.User;

public interface UserQueryRepository {
    User findByUsername(String username);
}
