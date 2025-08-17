package com.maxinhai.platform.handler;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Hash（哈希）操作
 */
@Component
public class HashHandler {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 向Hash添加字段
     */
    public void set(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 获取Hash中指定字段的值
     */
    public Object get(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 获取Hash中所有字段和值
     */
    public Map<Object, Object> getAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 删除Hash中的字段
     */
    public Long delete(String key, Object... hashKeys) {
        return redisTemplate.opsForHash().delete(key, hashKeys);
    }

}
