package com.memil.setting.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

// MEMIL QueryDsl 설정 클래스

@Component
public class QueryDslConfig {
    @PersistenceContext
    private EntityManager em;

    // JPAQueryFactory: 쿼리 빌드할 때 필요한 객체
    @Bean
    public JPAQueryFactory jpaRepository(){
        return new JPAQueryFactory(em);
    }
}
