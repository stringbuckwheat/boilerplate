package com.memil.setting.common.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

// QueryDsl 설정 클래스

@Component
public class QueryDslConfig {
    @PersistenceContext
    private EntityManager em;
    @Bean
    public JPAQueryFactory jpaRepository(){
        return new JPAQueryFactory(em);
    }
}
