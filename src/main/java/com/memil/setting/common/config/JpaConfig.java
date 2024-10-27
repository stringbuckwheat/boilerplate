package com.memil.setting.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing // Auditing 활성화 -> createdDate, updatedDate 자동 관리
public class JpaConfig {
}