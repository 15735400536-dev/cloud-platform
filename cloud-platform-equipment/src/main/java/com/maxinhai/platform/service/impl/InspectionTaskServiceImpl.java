package com.maxinhai.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxinhai.platform.mapper.InspectionTaskMapper;
import com.maxinhai.platform.po.InspectionTask;
import com.maxinhai.platform.service.InspectionTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InspectionTaskServiceImpl extends ServiceImpl<InspectionTaskMapper, InspectionTask> implements InspectionTaskService {
}
