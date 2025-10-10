package com.maxinhai.platform.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxinhai.platform.po.LoginUser;
import com.maxinhai.platform.service.TokenServiceImpl;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.JwtUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName：JwtAuthenticationSuccessHandler
 * @Author: XinHai.Ma
 * @Date: 2025/9/2 15:31
 * @Description: Spring Security 提供的认证成功后逻辑的扩展点
 */
@Component
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private TokenServiceImpl tokenServiceImpl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        // 生成JWT令牌
        String jwt = jwtUtils.generateJwtToken(authentication);

        // 将令牌存储到Redis，用于实现踢人功能
        tokenServiceImpl.saveUserToken(loginUser.getUsername(), jwt);

        // 获取用户角色
        List<String> roles = loginUser.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        // 构建响应
        AjaxResult ajaxResult = AjaxResult.success("authentication successful", jwt);

        // 设置响应内容
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(ajaxResult));
    }

}
