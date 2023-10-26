package com.memil.setting.repository;

import com.memil.setting.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String>, UserQueryRepository {
}
