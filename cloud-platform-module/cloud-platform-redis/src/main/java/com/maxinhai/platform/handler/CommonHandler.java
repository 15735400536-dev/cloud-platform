package com.maxinhai.platform.handler;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 通用操作
 */
@Component
public class CommonHandler {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 设置过期时间
     */
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 判断key是否存在
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 获取当前库所有key（简单但不推荐大量数据）
     * @param pattern 匹配模式，*表示所有
     * @return
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 通过SCAN命令渐进式获取所有符合模式的key（推荐生产环境使用）
     * @param pattern 匹配模式，*表示所有
     * @param count 每次扫描的数量
     * @return key集合
     */
    public List<String> scan(String pattern, int count) {
        List<String> result = new ArrayList<>();

        // 获取Redis连接（会自动释放，无需手动关闭）
        redisTemplate.execute((RedisConnection connection) -> {
            // 构建扫描选项
            ScanOptions options = ScanOptions.scanOptions()
                    .match(pattern)
                    .count(count)
                    .build();

            // 执行SCAN命令，获取游标
            Cursor<byte[]> cursor = connection.scan(options);

            // 遍历游标获取所有key
            while (cursor.hasNext()) {
                // 将字节数组转换为字符串（Redis存储key为字节数组）
                String key = new String(cursor.next());
                result.add(key);
            }

            // 关闭游标
            cursor.close();
            return null;
        });

        return result;
    }

}
