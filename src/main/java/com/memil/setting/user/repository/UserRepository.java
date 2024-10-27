package com.memil.setting.user.repository;

import com.memil.setting.common.enums.Provider;
import com.memil.setting.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserQueryRepository {
    Optional<User> findByEmailAndProvider(String email, Provider provider);
    Optional<User> findByUsername(String username);
}
