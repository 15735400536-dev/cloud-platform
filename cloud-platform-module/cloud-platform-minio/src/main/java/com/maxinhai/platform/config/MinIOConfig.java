package com.maxinhai.platform.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName：MinIOConfig
 * @Author: XinHai.Ma
 * @Date: 2025/9/12 10:57
 * @Description: MinIO配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinIOConfig {

    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
    private Integer urlExpire;

    /**
     * 初始化MinIO客户端
     */
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

}
