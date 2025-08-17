package com.maxinhai.platform.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxinhai.platform.dto.MaintenancePlanAddDTO;
import com.maxinhai.platform.dto.MaintenancePlanEditDTO;
import com.maxinhai.platform.dto.MaintenancePlanQueryDTO;
import com.maxinhai.platform.mapper.MaintenancePlanMapper;
import com.maxinhai.platform.po.MaintenancePlan;
import com.maxinhai.platform.service.MaintenancePlanService;
import com.maxinhai.platform.vo.MaintenancePlanVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MaintenancePlanServiceImpl extends ServiceImpl<MaintenancePlanMapper, MaintenancePlan> implements MaintenancePlanService {
    @Override
    public Page<MaintenancePlanVO> searchByPage(MaintenancePlanQueryDTO param) {
        return null;
    }

    @Override
    public MaintenancePlanVO getInfo(String id) {
        return null;
    }

    @Override
    public void remove(String[] ids) {

    }

    @Override
    public void edit(MaintenancePlanEditDTO param) {

    }

    @Override
    public void add(MaintenancePlanAddDTO param) {

    }
}
