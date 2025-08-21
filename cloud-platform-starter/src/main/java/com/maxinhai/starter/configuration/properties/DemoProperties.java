package com.maxinhai.starter.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName：DemoProperties
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 15:43
 * @Description: 自定义服务配置属性，绑定application.properties中前缀为"demo.log"的配置
 */
@Data
@ConfigurationProperties(prefix = "spring.maxinhai")
public class DemoProperties {

    // 是否启用日志服务
    private boolean enable = true;

}
