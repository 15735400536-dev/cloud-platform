package com.maxinhai.platform.config;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class SystemConfig {

    /**
     * 解决JDK9+访问模块信息的问题
     */
    @PostConstruct
    public void init() {
        String jdkVersion = System.getProperty("java.version");
        if (jdkVersion.startsWith("1.") || jdkVersion.startsWith("9") ||
                jdkVersion.startsWith("10") || jdkVersion.startsWith("11")) {
            // 针对特定JDK版本的处理
            System.setProperty("jdk.attach.allowAttachSelf", "true");
        }
    }

}
