package com.maxinhai.platform.listener;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.maxinhai.platform.dto.UserAddDTO;
import com.maxinhai.platform.feign.SystemFeignClient;
import com.maxinhai.platform.utils.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
//@Component
public class IninDataListener implements CommandLineRunner {

    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private SystemFeignClient systemFeignClient;

    @Override
    public void run(String... args) throws Exception {
        String account = DateUtil.format(new Date(), "yyyyMMddHHmmss");
        String username = IdUtil.fastSimpleUUID();

        UserAddDTO userAddDTO = new UserAddDTO();
        userAddDTO.setAccount(account);
        userAddDTO.setUsername(username);
        // 密码加密
        userAddDTO.setPassword(passwordEncoder.encode("123456"));
        AjaxResult<Void> ajaxResult = systemFeignClient.addUser(userAddDTO);
        if(ajaxResult.getCode() == HttpStatus.INTERNAL_SERVER_ERROR.value()){
            log.error("调用远程接口报错: {}", ajaxResult.getMsg());
        }

        log.info("创建用户 => 账号: {}, 用户名: {}", account, username);
    }
}
