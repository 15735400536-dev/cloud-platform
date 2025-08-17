package com.maxinhai.platform.annotations;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoRepeatSubmit {

    /**
     * 过期时间（默认5秒内不允许重复提交）
     */
    long expire() default 5;

    /**
     * 时间单位（默认秒）
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 重复提交提示信息
     */
    String message() default "请勿重复提交";

}
