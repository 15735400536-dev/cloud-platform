package com.maxinhai.starter.service.impl;

import com.maxinhai.starter.service.SayHelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @ClassName：SayHelloServiceImpl
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 15:57
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Slf4j
@Service
public class SayHelloServiceImpl implements SayHelloService {

    @Value("${spring.maxinhai.name:demo}")
    private String name;

    @Override
    @PostConstruct
    public void sayHello() {
        log.info("Demo Starter: {}, Hello !!!", name);
    }
}
