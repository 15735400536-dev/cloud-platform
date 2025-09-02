package com.maxinhai.platform.filter;

import com.maxinhai.platform.service.TokenServiceImpl;
import com.maxinhai.platform.service.UserDetailsServiceImpl;
import com.maxinhai.platform.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
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
        try {
            // 从请求中获取JWT令牌
            String jwt = parseJwt(request);

            // 验证令牌
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                // 从令牌中获取用户名
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                // 检查令牌是否已被踢除
                if (!tokenServiceImpl.isValidToken(username, jwt)) {
                    log.warn("User {} has been kicked out, token is invalid", username);
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You have been logged out by administrator");
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

}
