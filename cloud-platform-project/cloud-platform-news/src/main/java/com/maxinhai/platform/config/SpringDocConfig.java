package com.maxinhai.platform.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "项目API文档",
        version = "1.0",
        description = "SpringBoot项目接口文档"
))
public class SpringDocConfig {
    // 无需额外配置，注解已定义基本信息
}
