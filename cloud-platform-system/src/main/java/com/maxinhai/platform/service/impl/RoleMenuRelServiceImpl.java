package com.maxinhai.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxinhai.platform.po.RoleMenuRel;
import com.maxinhai.platform.mapper.RoleMenuRelMapper;
import com.maxinhai.platform.service.RoleMenuRelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RoleMenuRelServiceImpl extends ServiceImpl<RoleMenuRelMapper, RoleMenuRel>
        implements RoleMenuRelService {
}
