package com.maxinhai.platform.handler;

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * String（字符串）操作
 */
@Component
public class StringHandler {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 设置字符串值
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置字符串值并指定过期时间
     */
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 获取字符串值
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除key
     */
    public Boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 验证key是否存在
     *
     * @param key
     * @return
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 统计以指定前缀开头的键数量
     *
     * @param prefix 键前缀（如 "auth:token:"）
     * @return 匹配的键总数
     */
    public int scan(String prefix) {
        // 初始化计数器
        AtomicInteger count = new AtomicInteger(0);

        // 配置扫描参数：匹配前缀，每次预期返回1000个（非精确值）
        ScanOptions options = ScanOptions.scanOptions()
                .match(prefix + "*") // 匹配规则：前缀+通配符
                .count(1000) // 每次扫描的预期数量（加速扫描，非精确）
                .build();

        // 执行SCAN命令，通过RedisCallback获取游标
        try (Cursor<byte[]> cursor = redisTemplate.executeWithStickyConnection(redisConnection ->
                redisConnection.scan(options)
        )) {
            // 迭代游标，累加计数
            while (cursor.hasNext()) {
                cursor.next(); // 获取下一个键（无需处理键值，仅计数）
                count.incrementAndGet();
            }
        } catch (Exception e) {
            throw new RuntimeException("Redis SCAN命令执行失败", e);
        }

        return count.get();
    }

    /**
     * 扫描以指定前缀开头的键
     * @param prefix 键前缀（如 "auth:token:"）
     * @return 匹配的键集合
     */
    public List<String> scanKeysWithPrefix(String prefix) {
        // 用于存储所有匹配的key
        List<String> keys = new ArrayList<>();

        // 配置扫描参数：匹配前缀，每次预期返回1000个（非精确值）
        ScanOptions options = ScanOptions.scanOptions()
                .match(prefix + "*") // 匹配规则：前缀+通配符
                .count(1000) // 每次扫描的预期数量（加速扫描）
                .build();

        // 执行SCAN命令，获取游标（底层返回byte[]类型的key）
        try (Cursor<byte[]> cursor = redisTemplate.executeWithStickyConnection(redisConnection ->
                redisConnection.scan(options)
        )) {
            // 迭代游标，收集所有key（转换byte[]为String）
            while (cursor.hasNext()) {
                byte[] keyBytes = cursor.next();
                // Redis的key通常用UTF-8编码，转换为字符串
                String key = new String(keyBytes, StandardCharsets.UTF_8);
                keys.add(key);
            }
        } catch (Exception e) {
            throw new RuntimeException("Redis SCAN命令执行失败", e);
        }

        return keys;
    }

}
