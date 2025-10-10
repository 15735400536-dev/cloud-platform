package com.maxinhai.platform.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.MaintenanceTaskQueryDTO;
import com.maxinhai.platform.mapper.MaintenanceTaskMapper;
import com.maxinhai.platform.po.MaintenanceTask;
import com.maxinhai.platform.service.MaintenanceTaskService;
import com.maxinhai.platform.vo.MaintenanceTaskVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class MaintenanceTaskServiceImpl extends ServiceImpl<MaintenanceTaskMapper, MaintenanceTask>
        implements MaintenanceTaskService {

    @Resource
    private MaintenanceTaskMapper maintenanceTaskMapper;

    @Override
    public Page<MaintenanceTaskVO> searchByPage(MaintenanceTaskQueryDTO param) {
        return maintenanceTaskMapper.selectJoinPage(param.getPage(), MaintenanceTaskVO.class,
                new MPJLambdaWrapper<MaintenanceTask>()
                        .like(StrUtil.isNotBlank(param.getTaskCode()), MaintenanceTask::getTaskCode, param.getTaskCode())
                        .like(StrUtil.isNotBlank(param.getMaintenanceType()), MaintenanceTask::getMaintenanceType, param.getMaintenanceType())
                        .orderByDesc(MaintenanceTask::getCreateTime));
    }

    @Override
    public MaintenanceTaskVO getInfo(String id) {
        return maintenanceTaskMapper.selectJoinOne(MaintenanceTaskVO.class,
                new MPJLambdaWrapper<MaintenanceTask>()
                        .like(StrUtil.isNotBlank(id), MaintenanceTask::getId, id));
    }
}
