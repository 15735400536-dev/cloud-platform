package com.maxinhai.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxinhai.platform.mapper.MaintenanceTaskMapper;
import com.maxinhai.platform.po.MaintenanceTask;
import com.maxinhai.platform.service.MaintenanceTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MaintenanceTaskServiceImpl extends ServiceImpl<MaintenanceTaskMapper, MaintenanceTask>
        implements MaintenanceTaskService {
}
