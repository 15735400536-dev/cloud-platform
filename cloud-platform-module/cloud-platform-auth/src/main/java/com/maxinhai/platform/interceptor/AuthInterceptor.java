package com.maxinhai.platform.interceptor;

import com.alibaba.fastjson.JSON;
import com.maxinhai.platform.config.JwtConfig;
import com.maxinhai.platform.feign.SystemFeignClient;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.LoginUserContext;
import com.maxinhai.platform.vo.UserVO;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 认证拦截器，用于验证 Token 和获取当前登录用户信息
 */
@Component
public class AuthInterceptor implements HandlerInterceptor, Ordered {

    @Resource
    private JwtConfig jwtConfig;

    @Resource
    private SystemFeignClient systemFeignClient;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 从请求头中获取 Token
        String token = request.getHeader("Authorization");

        // 2. 检查 Token 是否存在
        if (token == null || token.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setCharacterEncoding("UTF-8"); // 强制响应流用 UTF-8 编码
            response.getWriter().write(JSON.toJSONString(AjaxResult.fail(HttpServletResponse.SC_UNAUTHORIZED, "未提供 Token", null)));
            return false;
        }

        // 3. 移除 Bearer 前缀（如果有）
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        try {
            // 4. 解析 Token 获取账号
            String account = jwtConfig.getAccountFromToken(token);

            // 5. 检查 Token 是否有效（与 Redis 中存储的 Token 比对）
            String redisToken = (String) redisTemplate.opsForValue().get("auth:token:" + account);
            if (redisToken == null || !redisToken.equals(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setCharacterEncoding("UTF-8"); // 强制响应流用 UTF-8 编码
                response.getWriter().write(JSON.toJSONString(AjaxResult.fail(HttpServletResponse.SC_UNAUTHORIZED, "Token 已失效，请重新登录", null)));
                return false;
            }

            // 6. 验证 Token
            if (!jwtConfig.validateToken(token, account)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setCharacterEncoding("UTF-8"); // 强制响应流用 UTF-8 编码
                response.getWriter().write(JSON.toJSONString(AjaxResult.fail(HttpServletResponse.SC_UNAUTHORIZED, "无效的 Token", null)));
                return false;
            }

            // 7. 获取用户信息并设置到 ThreadLocal 中
            UserVO user = systemFeignClient.findByAccount(account).getData();
            LoginUserContext.set(user.getId(), "userId", user.getId());
            LoginUserContext.set(user.getId(), "account", user.getAccount());
            LoginUserContext.set(user.getId(), "username", user.getUsername());
            return true;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setCharacterEncoding("UTF-8"); // 强制响应流用 UTF-8 编码
            response.getWriter().write(JSON.toJSONString(AjaxResult.success(HttpServletResponse.SC_OK, "Token 验证失败", null)));
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 1. 从请求头中获取 Token
        String token = request.getHeader("Authorization");
        // 2. 解析 Token 获取用户ID
        String userId = jwtConfig.getUserIdFromToken(token);
        // 3. 清除登录用户上下文
        LoginUserContext.remove(userId);
    }

    @Override
    public int getOrder() {
        // 明确最高优先级，比默认 0 更清晰
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
