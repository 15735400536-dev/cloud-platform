package com.maxinhai.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableFeignClients
public class EquipmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(EquipmentApplication.class, args);
    }

    /**
     * 模块规划：
     * 1.设备基本信息
     * 2.设备巡检项目（关联设备ID、关联巡检配置ID）
     * 3.设备巡检配置
     * 4.设备巡检计划（关联巡检配置ID）
     * 5.设备巡检任务（关联巡检计划ID）
     */

}
