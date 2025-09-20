package com.maxinhai.platform;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class ZlmApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ZlmApplication.class, args);
    }

//    @Resource
//    private ZlmClient zlmClient;

    @Override
    public void run(String... args) throws Exception {
//        zlmClient.setServerConfig(new ServiceConfigDTO("127.0.0.1", 8080, "EQPSv4wFVeS7e7hJ9YyC3s1Zbs8Njn78"));
//        log.info("设置服务器配置完成");
    }
}
