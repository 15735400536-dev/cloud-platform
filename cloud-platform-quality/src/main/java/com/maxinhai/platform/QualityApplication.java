package com.maxinhai.platform;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan(basePackages ="com.maxinhai.platform.mapper")
public class QualityApplication {

    public static void main(String[] args) {
        SpringApplication.run(QualityApplication.class, args);
    }

}
