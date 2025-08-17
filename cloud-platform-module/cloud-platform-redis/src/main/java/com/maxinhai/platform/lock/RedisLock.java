package com.maxinhai.platform.lock;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 */
@Component
public class RedisLock {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    // 锁的默认过期时间（30秒，防止死锁）
    private static final long DEFAULT_EXPIRE = 30;
    // 获取锁的默认超时时间（10秒）
    private static final long DEFAULT_TIMEOUT = 10;

    // 释放锁的Lua脚本（保证判断和删除的原子性）
    private static final String UNLOCK_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then " +
            "return redis.call('del', KEYS[1]) " +
            "else " +
            "return 0 " +
            "end";

    /**
     * 获取分布式锁
     * @param lockKey 锁的key
     * @return 锁的value（用于释放锁时校验），获取失败返回null
     */
    public String lock(String lockKey) {
        return lock(lockKey, DEFAULT_EXPIRE, DEFAULT_TIMEOUT);
    }

    /**
     * 带参数的获取分布式锁
     * @param lockKey 锁的key
     * @param expire 锁的过期时间（秒）
     * @param timeout 获取锁的超时时间（秒）
     * @return 锁的value，获取失败返回null
     */
    public String lock(String lockKey, long expire, long timeout) {
        // 生成唯一value（避免误释放其他线程的锁）
        String lockValue = UUID.randomUUID().toString();
        long start = System.currentTimeMillis();

        try {
            // 循环获取锁，直到超时
            while (true) {
                // 使用SET NX PX命令：key不存在时设置值，并指定过期时间
                Boolean success = redisTemplate.opsForValue().setIfAbsent(
                        lockKey,
                        lockValue,
                        expire,
                        TimeUnit.SECONDS
                );

                // 获取锁成功
                if (Boolean.TRUE.equals(success)) {
                    return lockValue;
                }

                // 计算剩余超时时间
                long end = System.currentTimeMillis();
                if (end - start > timeout * 1000) {
                    // 超时未获取到锁
                    return null;
                }

                // 短暂休眠后重试（避免频繁请求Redis）
                TimeUnit.MILLISECONDS.sleep(100);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        }
    }

    /**
     * 释放分布式锁
     * @param lockKey 锁的key
     * @param lockValue 获取锁时返回的value
     * @return 是否释放成功
     */
    public boolean unlock(String lockKey, String lockValue) {
        if (lockValue == null) {
            return false;
        }

        // 执行Lua脚本，保证判断和删除的原子性
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(UNLOCK_SCRIPT, Long.class);
        // 设置序列化器（避免key/value序列化问题）
        //redisTemplate.setKeySerializer(new StringRedisSerializer());
        //redisTemplate.setValueSerializer(new StringRedisSerializer());

        // 执行脚本：KEYS[1]是lockKey，ARGV[1]是lockValue
        Long result = redisTemplate.execute(
                redisScript,
                Collections.singletonList(lockKey),
                lockValue
        );

        // 结果为1表示释放成功
        return result != null && result == 1;
    }

}
