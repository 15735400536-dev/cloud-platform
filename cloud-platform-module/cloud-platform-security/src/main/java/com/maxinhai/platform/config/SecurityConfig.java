package com.maxinhai.platform.config;

import com.maxinhai.platform.exception.JwtAuthenticationEntryPoint;
import com.maxinhai.platform.filter.JwtAuthenticationFilter;
import com.maxinhai.platform.filter.JwtAuthorizationFilter;
import com.maxinhai.platform.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // 启用方法级别的权限控制
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private UserDetailsServiceImpl userDetailsService;

    @Resource
    private JwtAuthorizationFilter jwtAuthorizationFilter;

    @Resource
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                // 未授权处理
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                // 无状态会话，不创建Session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // 配置URL访问权限
                .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll() // 登录注册接口允许匿名访问
                .antMatchers("/api/test/**").permitAll() // 测试接口
                .anyRequest().authenticated(); // 其他所有接口需要认证

        // 添加JWT登录过滤器
        http.addFilter(new JwtAuthenticationFilter(authenticationManager(http.getSharedObject(AuthenticationConfiguration.class))));
        // 添加JWT授权过滤器
        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
