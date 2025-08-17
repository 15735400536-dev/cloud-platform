package com.maxinhai.platform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private UserDetailsService userDetailsService; // 自定义用户服务（见下文）

    // 密码加密器
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 会话注册表（用于强制退出功能）
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 配置用户认证逻辑
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // 前后端分离场景下关闭CSRF
                .cors() // 允许跨域（如需）
                .and()
                .authorizeRequests()
                // 开放注册、登录接口
                .antMatchers("/api/auth/register", "/api/auth/login").permitAll()
                // 其他接口需要认证
                .anyRequest().authenticated()
                .and()
                // 表单登录（自定义登录接口）
                .formLogin()
                .loginProcessingUrl("/api/auth/login") // 登录请求路径
                .successHandler((request, response, authentication) -> {
                    // 登录成功返回JSON
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"code\":200,\"msg\":\"登录成功\"}");
                })
                .failureHandler((request, response, exception) -> {
                    // 登录失败返回JSON
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"code\":401,\"msg\":\"用户名或密码错误\"}");
                })
                .and()
                // 注销配置（用于强制退出）
                .logout()
                .logoutUrl("/api/auth/logout") // 注销请求路径
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write("{\"code\":200,\"msg\":\"退出成功\"}");
                })
                .and()
                // 会话管理（用于强制退出）
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .maximumSessions(1) // 单用户最多1个会话
                .sessionRegistry(sessionRegistry()); // 绑定会话注册表
    }

}
