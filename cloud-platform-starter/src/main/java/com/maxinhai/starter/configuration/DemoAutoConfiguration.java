package com.maxinhai.starter.configuration;

import com.maxinhai.starter.configuration.properties.DemoProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @ClassName：DemoAutoConfiguration
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 15:43
 * @Description: 自动配置类，Spring Boot启动时会自动扫描并加载此类
 */
@Order(value = 1) // 指定组件的执行顺序或加载优先级（数值越小，优先级越高）
@Configuration
@EnableConfigurationProperties(DemoProperties.class) // 启用配置属性绑定
@ConditionalOnProperty(
        prefix = "spring.maxinhai", // 指定配置属性的前缀，即配置项的命名空间，例如：需要匹配的配置项是 spring.maxinhai.enable
        name = "enable", // 指定具体的属性名，与前缀组合后形成完整的配置项 key：spring.maxinhai.enable
        havingValue = "true", // 指定属性的期望值。当配置项的值等于 havingValue 时，条件成立（允许创建 Bean）。这里表示：当 spring.maxinhai.demo = true 时，条件满足
        matchIfMissing = true // 当配置文件中不存在该属性时，是否默认匹配成功。
)
@ComponentScan("com.maxinhai.starter")
public class DemoAutoConfiguration {
}
