package com.maxinhai.platform.annotations;

import java.lang.annotation.*;

/**
 * 接口限流注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {

    /**
     * 限流阈值（每秒允许的请求数）
     */
    double rate() default 10.0;

    /**
     * 限流提示信息
     */
    String message() default "接口请求过于频繁，请稍后再试";

}
