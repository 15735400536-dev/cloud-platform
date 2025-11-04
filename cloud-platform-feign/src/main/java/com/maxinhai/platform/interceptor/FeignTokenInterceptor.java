package com.maxinhai.platform.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Feign拦截器：注入Spring容器，全局生效
 */
@Slf4j
@Component
public class FeignTokenInterceptor implements RequestInterceptor {

    // Token请求头Key（通常为Authorization，也可自定义如X-Token）
    private static final String TOKEN_HEADER = "Authorization";
    // Token前缀（若为Bearer Token，需加"Bearer "前缀，否则直接传Token）
    private static final String TOKEN_PREFIX = "Bearer ";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        // 1. 获取当前请求上下文（仅在同线程的Web请求中有效）
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            // 2. 从请求中获取Token（如Authorization头）
            HttpServletRequest request = attributes.getRequest();
            String token = request.getHeader(TOKEN_HEADER);

            // 3. 将Token添加到Feign请求头
            if (token != null && !token.isEmpty()) {
                log.debug("FeignTokenInterceptor => token: {}", token);
                requestTemplate.header(TOKEN_HEADER, token);
            } else  {
                requestTemplate.header(TOKEN_HEADER, "internal");
            }
        }
    }

}
