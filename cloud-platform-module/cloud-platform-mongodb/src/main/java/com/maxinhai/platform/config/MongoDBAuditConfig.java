package com.maxinhai.platform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import java.util.Optional;

/**
 * @ClassName：MongoDBAuditConfig
 * @Author: XinHai.Ma
 * @Date: 2025/9/3 13:26
 * @Description: MongoDB审计配置类，启用审计功能并配置审计者信息
 */
@Configuration
@EnableMongoAuditing(auditorAwareRef = "auditorAware")
public class MongoDBAuditConfig {

    /**
     * 配置审计者提供者
     * 实际应用中可以从当前登录用户上下文中获取用户名
     */
    @Bean
    public AuditorAware<String> auditorAware() {
        // 这里只是示例，实际项目中应从SecurityContext等获取当前登录用户
        return () -> Optional.of("system"); // 默认使用"system"作为操作人
    }

}
