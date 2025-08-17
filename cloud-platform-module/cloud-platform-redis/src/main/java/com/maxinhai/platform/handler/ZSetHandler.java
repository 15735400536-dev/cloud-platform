package com.maxinhai.platform.handler;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * ZSet（有序集合）操作
 */
@Component
public class ZSetHandler {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 向ZSet添加元素（带分数）
     */
    public Boolean add(String key, Object value, double score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 获取ZSet中元素的分数
     */
    public Double score(String key, Object value) {
        return redisTemplate.opsForZSet().score(key, value);
    }

    /**
     * 按分数范围查询ZSet元素（升序）
     */
    public Set<Object> rangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    /**
     * 删除ZSet中的元素
     */
    public Long remove(String key, Object... values) {
        return redisTemplate.opsForZSet().remove(key, values);
    }

}
