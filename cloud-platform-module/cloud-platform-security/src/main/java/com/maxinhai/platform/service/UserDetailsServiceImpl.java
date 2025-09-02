package com.maxinhai.platform.service;

import com.maxinhai.platform.feign.SystemFeignClient;
import com.maxinhai.platform.po.LoginUser;
import com.maxinhai.platform.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    @Resource
    private SystemFeignClient systemFeignClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserVO user = systemFeignClient.findByAccount(username).getData();
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return LoginUser.convert(user); // 返回自定义User实体（已实现UserDetails）
    }

}
