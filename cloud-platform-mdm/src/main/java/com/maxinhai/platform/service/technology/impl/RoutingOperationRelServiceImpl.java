package com.maxinhai.platform.service.technology.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxinhai.platform.mapper.RoutingOperationRelMapper;
import com.maxinhai.platform.po.technology.RoutingOperationRel;
import com.maxinhai.platform.service.technology.RoutingOperationRelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RoutingOperationRelServiceImpl extends ServiceImpl<RoutingOperationRelMapper, RoutingOperationRel>
        implements RoutingOperationRelService {
}
