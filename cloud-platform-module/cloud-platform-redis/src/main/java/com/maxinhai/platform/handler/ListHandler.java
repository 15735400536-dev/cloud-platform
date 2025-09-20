package com.maxinhai.platform.handler;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * List（列表）操作
 */
@Component
public class ListHandler {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 向List左侧（头部）添加元素
     * @param key 键
     * @param values 元素（可多个）
     * @return 新增后List的长度
     */
    public Long leftPushAll(String key, Object... values) {
        return redisTemplate.opsForList().leftPushAll(key, values);
    }

    /**
     * 向List左侧（头部）添加元素
     * @param key 键
     * @param value 元素
     * @return 新增后List的长度
     */
    public Long leftPush(String key, String value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 向List右侧（尾部）添加元素
     * @param key 键
     * @param values 元素（可多个）
     * @return 新增后List的长度
     */
    public Long rightPushAll(String key, Object... values) {
        return redisTemplate.opsForList().rightPushAll(key, values);
    }

    /**
     * 向List右侧（尾部）添加元素
     * @param key 键
     * @param value 元素
     * @return 新增后List的长度
     */
    public Long rightPush(String key, String value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 从List左侧（头部）移除并返回第一个元素
     * @param key 键
     * @return 移除的元素
     */
    public Object leftPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 从List左侧（头部）移除并返回多个元素（6.2.0版本之后支持该语法）
     * @param key
     * @param count
     * @return
     */
    public List<Object> batchLeftPop(String key, long count) {
        return redisTemplate.opsForList().leftPop(key, count);
    }

    /**
     * 从List右侧（尾部）移除并返回最后一个元素
     * @param key 键
     * @return 移除的元素
     */
    public Object rightPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 从List右侧（尾部）移除并返回多个元素（6.2.0版本之后支持该语法）
     * @param key
     * @param count
     * @return
     */
    public List<Object>  batchRightPop(String key, long count) {
        return redisTemplate.opsForList().rightPop(key, count);
    }

    /**
     * 获取List指定索引位置的元素
     * @param key 键
     * @param index 索引（0为第一个元素，-1为最后一个元素）
     * @return 该位置的元素
     */
    public Object index(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    /**
     * 获取List指定范围的元素
     * @param key 键
     * @param start 起始索引（0为第一个元素）
     * @param end 结束索引（-1为最后一个元素）
     * @return 元素列表
     */
    public List<Object> range(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 获取List的长度
     * @param key 键
     * @return 长度
     */
    public Long size(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 截取List，只保留指定范围的元素（超出范围的元素会被删除）
     * @param key 键
     * @param start 起始索引
     * @param end 结束索引
     */
    public void trim(String key, long start, long end) {
        redisTemplate.opsForList().trim(key, start, end);
    }

    /**
     * 删除List中指定次数的元素
     * @param key 键
     * @param count 删除次数（正数：从头部开始删除count个；负数：从尾部开始删除count个；0：删除所有相同元素）
     * @param value 要删除的元素
     * @return 实际删除的数量
     */
    public Long remove(String key, long count, Object value) {
        return redisTemplate.opsForList().remove(key, count, value);
    }

    /**
     * 修改List中指定索引位置的元素
     * @param key 键
     * @param index 索引
     * @param value 新值
     */
    public void set(String key, long index, Object value) {
        redisTemplate.opsForList().set(key, index, value);
    }


}
