package com.maxinhai.platform.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxinhai.platform.utils.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName：JwtAuthenticationEntryPoint
 * @Author: XinHai.Ma
 * @Date: 2025/9/2 15:27
 * @Description: 处理 “未认证用户尝试访问受保护资源” 的场景
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.error("Path: {}, Unauthorized error: {}", request.getRequestURI(), authException.getMessage());

//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Please Login");

        AjaxResult ajaxResult = AjaxResult.fail(HttpServletResponse.SC_UNAUTHORIZED, "Please Login", "");
        // 设置响应内容
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(ajaxResult));
    }

}
