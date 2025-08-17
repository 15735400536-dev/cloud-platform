package com.maxinhai.platform.handler;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * Set（集合）操作
 */
@Component
public class SetHandler {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 向Set添加元素
     */
    public Long add(String key, Object... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    /**
     * 获取Set所有元素
     */
    public Set<Object> members(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 判断元素是否在Set中
     */
    public Boolean isMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * 删除Set中的元素
     */
    public Long remove(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

}
