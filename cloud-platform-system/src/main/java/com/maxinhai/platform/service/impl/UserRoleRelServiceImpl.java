package com.maxinhai.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxinhai.platform.po.UserRoleRel;
import com.maxinhai.platform.mapper.UserRoleRelMapper;
import com.maxinhai.platform.service.UserRoleRelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserRoleRelServiceImpl extends ServiceImpl<UserRoleRelMapper, UserRoleRel>
        implements UserRoleRelService {

}
