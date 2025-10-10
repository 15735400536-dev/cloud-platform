package com.maxinhai.platform.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxinhai.platform.utils.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName：AuthenticationFailureHandlerImpl
 * @Author: XinHai.Ma
 * @Date: 2025/9/2 15:57
 * @Description: Spring Security 提供的处理认证失败后的逻辑
 */
@Slf4j
@Component
public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        // 1.记录失败日志
        String username = request.getParameter("username");
        String ip = request.getRemoteAddr();
        log.warn("用户 [{}] 从 IP [{}] 登录失败：{}", username, ip, exception.getMessage());

        // 2.构建响应
        AjaxResult ajaxResult = AjaxResult.success("authentication failed", getErrorMessage(exception));

        // 设置响应内容
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(ajaxResult));
    }

    /**
     * 根据异常类型返回友好提示
     * @param exception
     * @return
     */
    private String getErrorMessage(AuthenticationException exception) {
        if (exception instanceof BadCredentialsException) {
            return "用户名或密码错误";
        } else if (exception instanceof AccountExpiredException) {
            return "账号已过期";
        } else if (exception instanceof LockedException) {
            return "账号已锁定";
        } else {
            return "登录失败，请稍后重试";
        }
    }
}
