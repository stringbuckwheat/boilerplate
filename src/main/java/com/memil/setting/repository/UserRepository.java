package com.memil.setting.repository;

import com.memil.setting.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String>, UserQueryRepository {
    Optional<User> findByUsernameAndProvider(String username, String provider);
}
