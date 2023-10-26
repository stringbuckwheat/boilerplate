package com.memil.setting.repository;

import com.memil.setting.entity.QUser;
import com.memil.setting.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import static com.memil.setting.entity.QUser.user;

// MEMIL static import

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserQueryRepositoryImpl implements UserQueryRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public User findByUsername(String username) {
        log.debug("QueryDsl findByUsername");

        return queryFactory
                .selectFrom(user) // MEMIL static import
                .where(user.username.eq(username))
                .fetchOne();
    }
}
