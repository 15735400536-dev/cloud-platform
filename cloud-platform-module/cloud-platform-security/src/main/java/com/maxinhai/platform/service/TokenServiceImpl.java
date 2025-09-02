package com.maxinhai.platform.service;

import com.maxinhai.platform.config.JwtProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName：RedisService
 * @Author: XinHai.Ma
 * @Date: 2025/9/2 15:32
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Slf4j
@Service
public class TokenServiceImpl {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    // Redis中存储用户令牌的键前缀
    private static final String USER_TOKEN_KEY_PREFIX = "user:token:";

    // 令牌过期时间（与JWT过期时间一致）
    @Resource
    private JwtProperties jwtProperties;

    /**
     * 保存用户令牌
     */
    public void saveUserToken(String username, String token) {
        String key = USER_TOKEN_KEY_PREFIX + username;
        redisTemplate.opsForValue().set(key, token, jwtProperties.getJwtExpirationMs(), TimeUnit.MILLISECONDS);
    }

    /**
     * 验证令牌是否有效
     */
    public boolean isValidToken(String username, String token) {
        String key = USER_TOKEN_KEY_PREFIX + username;
        Object storedToken = redisTemplate.opsForValue().get(key);
        return storedToken != null && storedToken.equals(token);
    }

    /**
     * 移除用户令牌（踢人）
     */
    public void removeUserToken(String username) {
        String key = USER_TOKEN_KEY_PREFIX + username;
        redisTemplate.delete(key);
    }

    /**
     * 获取用户当前的令牌
     */
    public String getUserToken(String username) {
        String key = USER_TOKEN_KEY_PREFIX + username;
        Object token = redisTemplate.opsForValue().get(key);
        return token != null ? token.toString() : null;
    }

}
