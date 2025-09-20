package com.maxinhai.platform.config;

import com.maxinhai.platform.constants.FileConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置静态资源访问路径，使上传的文件可以通过URL访问
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:" + FileConstants.UPLOAD_DIR + "/");
    }

}
