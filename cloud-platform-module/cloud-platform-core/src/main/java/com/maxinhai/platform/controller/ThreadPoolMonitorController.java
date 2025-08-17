package com.maxinhai.platform.controller;

import com.maxinhai.platform.config.ThreadPoolConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/monitor")
public class ThreadPoolMonitorController {

    @GetMapping("/threadpool")
    public ThreadPoolConfig.ThreadPoolStats getThreadPoolStats() {
        return ThreadPoolConfig.getThreadPoolStats();
    }

}
