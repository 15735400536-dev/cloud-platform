package com.maxinhai.platform.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName：JwtProperties
 * @Author: XinHai.Ma
 * @Date: 2025/9/2 15:25
 * @Description: Jwt配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "platform.jwt")
public class JwtProperties {

    private String jwtSecret;
    private int jwtExpirationMs;

}
