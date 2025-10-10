package com.maxinhai.platform.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * 开启JPA审计功能，支持自动填充创建时间、修改时间、创建人、修改人等字段
 */
@Configuration
@EnableJpaAuditing // 关键注解：启用JPA审计
public class JpaAuditingConfig {
}
