package com.maxinhai.platform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName：RedisCacheConfig
 * @Author: XinHai.Ma
 * @Date: 2025/9/29 11:26
 * @Description: 缓存管理器配置
 */
@Configuration
public class RedisCacheConfig {

    // 自定义Redis缓存管理器，解决序列化问题
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        // 1. 全局默认配置（key用String序列化，value用JSON序列化）
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(1))  // 默认缓存过期时间1小时
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))  // key序列化
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()))  // value序列化（JSON）
                .disableCachingNullValues();  // 不缓存null值

        // 2. 针对不同缓存名称的个性化配置（如用户缓存过期30分钟，角色缓存过期2小时）
        Map<String, RedisCacheConfiguration> cacheConfigs = new HashMap<>();
        cacheConfigs.put("user", defaultConfig.entryTtl(Duration.ofMinutes(30)));  // 缓存名称"user"的过期时间
        cacheConfigs.put("role", defaultConfig.entryTtl(Duration.ofHours(2)));    // 缓存名称"role"的过期时间
        cacheConfigs.put("menu", defaultConfig.entryTtl(Duration.ofHours(2)));    // 缓存名称"menu"的过期时间

        // 3. 创建缓存管理器
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultConfig)  // 应用默认配置
                .withInitialCacheConfigurations(cacheConfigs)  // 应用个性化配置
                .build();
    }

}
