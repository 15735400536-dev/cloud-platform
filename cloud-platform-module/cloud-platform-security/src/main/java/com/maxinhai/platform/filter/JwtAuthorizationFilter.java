package com.maxinhai.platform.filter;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxinhai.platform.service.TokenServiceImpl;
import com.maxinhai.platform.service.UserDetailsServiceImpl;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName：JwtAuthorizationFilter
 * @Author: XinHai.Ma
 * @Date: 2025/9/2 15:29
 * @Description: token校验过滤器
 */
@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private UserDetailsServiceImpl userDetailsService;

    @Resource
    private TokenServiceImpl tokenServiceImpl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 对于不需要认证的路径（如登录、注册、Swagger文档），直接放行
        String requestUri = request.getRequestURI();
        String contextPath = request.getContextPath(); // 项目上下文路径（如 /platform）
        String fullPath = request.getRequestURL().toString(); // 完整URL（如 http://localhost:20011/platform/api/auth/login）
        log.info("当前请求：contextPath={}, URI={}, 完整URL={}", contextPath, requestUri, fullPath);

        if (isPublicPath(requestUri)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 从请求中获取JWT令牌
            String jwt = parseJwt(request);

            if (StrUtil.isEmpty(jwt)) {
                log.warn("User not login, access path: {}", requestUri);

                AjaxResult ajaxResult = AjaxResult.fail(HttpServletResponse.SC_UNAUTHORIZED, "Please bring your token", null);
                // 设置响应内容
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(new ObjectMapper().writeValueAsString(ajaxResult));

                // 不发送错误，让Spring Security的默认机制处理
                filterChain.doFilter(request, response);
                return;
            }

            // 验证令牌
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                // 从令牌中获取用户名
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                // 检查令牌是否已被踢除
                if (!tokenServiceImpl.isValidToken(username, jwt)) {
                    log.warn("User {} has been kicked out, token is invalid", username);
                    //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You have been logged out by administrator");

                    AjaxResult ajaxResult = AjaxResult.fail(HttpServletResponse.SC_UNAUTHORIZED, "You have been logged out by administrator", null);
                    // 设置响应内容
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(new ObjectMapper().writeValueAsString(ajaxResult));
                    filterChain.doFilter(request, response);
                    return;
                }

                // 加载用户详情
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 设置认证信息到上下文
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
    }

    // 从请求头中解析JWT令牌
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }

    // 定义不需要认证的公共路径
    private boolean isPublicPath(String uri) {
        boolean isPublic = uri.startsWith("/api/auth/") ||
                uri.startsWith("/api/test/") ||
                uri.startsWith("/doc.html") ||
                uri.startsWith("/webjars/") ||
                uri.startsWith("/v2/api-docs/") ||
                uri.startsWith("/v3/api-docs/") ||
                uri.startsWith("/swagger-resources/") ||
                uri.startsWith("/swagger-ui/") ||
                uri.startsWith("/error");
        log.info("路径 [{}] 是否为公开路径：{}", uri, isPublic);
        return isPublic;
    }

}
