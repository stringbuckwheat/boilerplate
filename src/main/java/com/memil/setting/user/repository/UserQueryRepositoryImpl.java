package com.memil.setting.user.repository;

import com.memil.setting.user.model.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserQueryRepositoryImpl implements UserQueryRepository{
    private final JPAQueryFactory queryFactory;

//    @Override
//    public Optional<User> findByUsername(String username) {
//        User result = queryFactory
//                .selectFrom(QUser)
//                .where(user.username.eq(username))
//                .fetchOne();
//
//        return Optional.ofNullable(result);
//    }
}
