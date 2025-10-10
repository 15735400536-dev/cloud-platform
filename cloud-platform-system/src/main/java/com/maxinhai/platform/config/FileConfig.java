package com.maxinhai.platform.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName：FileConfig
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 14:10
 * @Description: 文件配置类，设置上传限制和静态资源映射
 */
//@Configuration
public class FileConfig implements WebMvcConfigurer {

    @Value("${file.upload.path}")
    private String uploadPath;

    @Value("${file.access.path}")
    private String accessPath;

    /**
     * 配置静态资源代理访问
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 将访问路径映射到实际文件存储路径
        registry.addResourceHandler(accessPath + "/**")
                .addResourceLocations("file:" + uploadPath + "/");
    }

}
