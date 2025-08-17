package com.maxinhai.platform.aspect;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.maxinhai.platform.annotations.NoRepeatSubmit;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 防重复提交切面（基于Redis实现）
 */
@Slf4j
@Aspect
@Component
public class NoRepeatSubmitAspect {

//    @Resource
//    private RedisTemplate<String, Object> redisTemplate;

    @Pointcut("@annotation(noRepeatSubmit)")
    public void pointcut(NoRepeatSubmit noRepeatSubmit) {}

    @Around("pointcut(noRepeatSubmit)")
    public Object around(ProceedingJoinPoint joinPoint, NoRepeatSubmit noRepeatSubmit) throws Throwable {
        // 1. 生成唯一标识（用户标识+接口路径+请求参数哈希，确保同一请求唯一）
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String userId = getCurrentUserId(); // 实际场景从登录信息中获取用户ID
        String path = request.getRequestURI();
        String params = DigestUtil.md5Hex(StrUtil.toString(joinPoint.getArgs())); // 参数哈希
        String submitKey = "repeat_submit:" + userId + ":" + path + ":" + params;

        // 2. 尝试设置Redis键（原子操作：如果键不存在则设置，返回true）
//        Boolean isFirstSubmit = redisTemplate.opsForValue().setIfAbsent(
//                submitKey,
//                "1",
//                noRepeatSubmit.expire(),
//                noRepeatSubmit.timeUnit()
//        );

//        if (Boolean.TRUE.equals(isFirstSubmit)) {
//            // 第一次提交，正常执行
//            return joinPoint.proceed();
//        } else {
//            // 重复提交，抛出异常
//            log.warn("重复提交：用户={}, 路径={}", userId, path);
//            throw new RuntimeException(noRepeatSubmit.message());
//        }
        return null;
    }

    // 模拟获取当前用户ID（实际应从SecurityContext或Token中获取）
    private String getCurrentUserId() {
        return "default_user"; // 实际场景替换为真实用户标识
    }

}
