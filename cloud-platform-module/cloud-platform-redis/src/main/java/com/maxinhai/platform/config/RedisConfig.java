package com.maxinhai.platform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    /**
     * 自定义RedisTemplate，支持String、对象等类型的JSON序列化
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // 字符串序列化器（key和hashKey使用）
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        // JSON序列化器（value和hashValue使用，支持对象自动序列化）
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer();

        // 设置key序列化方式
        template.setKeySerializer(stringSerializer);
        // 设置value序列化方式
        template.setValueSerializer(jsonSerializer);
        // 设置hashKey序列化方式
        template.setHashKeySerializer(stringSerializer);
        // 设置hashValue序列化方式
        template.setHashValueSerializer(jsonSerializer);

        template.afterPropertiesSet();
        return template;
    }

}
