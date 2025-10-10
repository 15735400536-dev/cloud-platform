package com.maxinhai.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxinhai.platform.mapper.DeptUserRelMapper;
import com.maxinhai.platform.po.DeptUserRel;
import com.maxinhai.platform.service.DeptUserRelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @ClassName：DeptUserRelServiceImpl
 * @Author: XinHai.Ma
 * @Date: 2025/9/28 14:57
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Slf4j
@Service
public class DeptUserRelServiceImpl extends ServiceImpl<DeptUserRelMapper, DeptUserRel>
        implements DeptUserRelService {
}
