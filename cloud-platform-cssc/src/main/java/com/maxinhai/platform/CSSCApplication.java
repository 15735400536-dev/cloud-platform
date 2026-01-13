package com.maxinhai.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @ClassName：CSSCApplication
 * @Author: XinHai.Ma
 * @Date: 2026/1/13 21:58
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@SpringBootApplication
@EnableScheduling
@EnableFeignClients
public class CSSCApplication {

    public static void main(String[] args) {
        SpringApplication.run(CSSCApplication.class, args);
    }

}
