package com.maxinhai.platform.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName：DemoController
 * @Author: XinHai.Ma
 * @Date: 2025/9/3 15:45
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("success");
    }

    @GetMapping("/test") // 无任何前缀，最简单路径
    public String hello1() {
        return "hello world";
    }

}
