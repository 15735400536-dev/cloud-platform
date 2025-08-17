package com.maxinhai.platform.aspect;

import com.maxinhai.platform.annotations.RateLimit;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 限流切面（基于Redis+令牌桶思想）
 */
@Slf4j
@Aspect
@Component
public class RateLimitAspect {

//    @Resource
//    private RedisTemplate<String, Object> redisTemplate;

    // 单机锁（防止Redis操作并发问题）
    private final ReentrantLock lock = new ReentrantLock();

    @Pointcut("@annotation(rateLimit)")
    public void pointcut(RateLimit rateLimit) {}

    @Around("pointcut(rateLimit)")
    public Object around(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        // 1. 生成唯一限流键（用户IP+接口路径，确保不同用户/接口独立限流）
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getRemoteAddr();
        String path = request.getRequestURI();
        String limitKey = "rate_limit:" + ip + ":" + path;

        // 2. 基于Redis实现令牌桶限流（简化版：通过计数器+过期时间实现）
        double rate = rateLimit.rate();
        long expireSeconds = 1; // 1秒窗口

        lock.lock();
        try {
//            // 获取当前窗口内的请求数
//            Long count = redisTemplate.opsForValue().increment(limitKey, 1);
//            if (count == 1) {
//                // 第一次请求，设置过期时间（1秒后重置）
//                redisTemplate.expire(limitKey, expireSeconds, TimeUnit.SECONDS);
//            }
//
//            // 3. 判断是否超过限流阈值
//            if (count > rate) {
//                log.warn("接口限流：IP={}, 路径={}, 超过阈值={}", ip, path, rate);
//                throw new RuntimeException(rateLimit.message());
//            }
        } finally {
            lock.unlock();
        }

        // 4. 正常执行接口
        return joinPoint.proceed();
    }

}
