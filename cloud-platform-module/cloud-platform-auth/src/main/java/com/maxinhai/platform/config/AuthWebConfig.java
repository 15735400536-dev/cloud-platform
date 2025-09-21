package com.maxinhai.platform.config;

import com.maxinhai.platform.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * Web 配置类，用于注册拦截器
 */
@Configuration
public class AuthWebConfig implements WebMvcConfigurer {

    @Lazy
    @Resource
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册认证拦截器
        registry.addInterceptor(authInterceptor)
                // 拦截所有请求
                .addPathPatterns("/**")
                // 1. 排除登录和注册接口
                .excludePathPatterns("/api/auth/login", "/api/auth/register")
                // 2. 排除 Swagger 2.x 接口文档路径
                .excludePathPatterns(
                        "/swagger-ui.html",          // Swagger 2.x 首页
                        "/swagger-resources/**",      // Swagger 2.x 资源路径
                        "/v2/api-docs",               // Swagger 2.x API 文档数据
                        "/v2/api-docs-ext"            // Swagger 2.x 扩展数据（部分框架用）
                )
                // 3. 排除 OpenAPI 3.x（SpringDoc）路径
                .excludePathPatterns(
                        "/swagger-ui/**",             // OpenAPI 3.x 首页（注意是 /**，不是 .html）
                        "/v3/api-docs/**",            // OpenAPI 3.x API 文档数据（支持分组）
                        "/swagger-ui/index.html"      // 部分版本的 OpenAPI 3.x 首页
                )
                // 4. 排除 Knife4j 路径（若用 Knife4j，可覆盖上述 Swagger 路径）
                .excludePathPatterns(
                        "/doc.html",                  // Knife4j 首页
                        "/webjars/**"                 // Knife4j 依赖的前端资源（如 JS/CSS）
                )
                // 5. 排除 根据账号查询用户信息接口
                .excludePathPatterns(
                        "/user/findByAccount/**",
                        "/codeRule/generateCode/**"
                );
    }

}
