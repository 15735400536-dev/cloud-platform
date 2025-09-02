package com.maxinhai.platform.controller;

import com.maxinhai.platform.dto.UserLoginDTO;
import com.maxinhai.platform.feign.SystemFeignClient;
import com.maxinhai.platform.po.LoginUser;
import com.maxinhai.platform.service.TokenServiceImpl;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.JwtUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName：AuthController
 * @Author: XinHai.Ma
 * @Date: 2025/9/2 16:23
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private TokenServiceImpl tokenService;

    @Resource
    private SystemFeignClient systemFeignClient;

    @Resource
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public AjaxResult<String> login(@Valid @RequestBody UserLoginDTO param) {
        // 认证用户
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(param.getUsername(), param.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 生成JWT令牌
        String jwt = jwtUtils.generateJwtToken(authentication);

        // 获取用户信息
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        List<String> roles = loginUser.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        // 保存令牌到Redis
        tokenService.saveUserToken(loginUser.getUsername(), jwt);

        return AjaxResult.success("login success", jwt);
    }

    // 管理员才能踢人
    @PreAuthorize("hasAnyRole('super_admin','admin')")
    @PostMapping("/kick/{username}")
    public AjaxResult<String> kick(@PathVariable String username) {
        // 检查用户是否存在
        if (!systemFeignClient.existByUsername(username).getData()) {
            return AjaxResult.fail("user not found!");
        }

        // 移除用户的令牌，使其失效
        tokenService.removeUserToken(username);

        return AjaxResult.success("user " + username + " has been kicked out successfully!");
    }

    // 获取当前登录用户信息
    @GetMapping("/getCurrentUser")
    @PreAuthorize("isAuthenticated()")
    public AjaxResult<Map<String, String>> getCurrentUser() {
        // 1. 获取认证对象
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return AjaxResult.success("not user login"); // 未登录
        }

        // 2. 获取用户主体（通常是 UserDetails 实例）
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Map<String, String> result = new HashMap<>();
        result.put("userId", loginUser.getId());
        result.put("username", loginUser.getUsername());
        result.put("password", loginUser.getPassword());
        return AjaxResult.success(result);
    }

}
