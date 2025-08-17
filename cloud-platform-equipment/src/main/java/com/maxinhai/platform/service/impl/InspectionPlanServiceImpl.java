package com.maxinhai.platform.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxinhai.platform.dto.InspectionPlanAddDTO;
import com.maxinhai.platform.dto.InspectionPlanEditDTO;
import com.maxinhai.platform.dto.InspectionPlanQueryDTO;
import com.maxinhai.platform.mapper.InspectionPlanMapper;
import com.maxinhai.platform.po.InspectionPlan;
import com.maxinhai.platform.service.InspectionPlanService;
import com.maxinhai.platform.vo.InspectionPlanVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InspectionPlanServiceImpl extends ServiceImpl<InspectionPlanMapper, InspectionPlan> implements InspectionPlanService {
    @Override
    public Page<InspectionPlanVO> searchByPage(InspectionPlanQueryDTO param) {
        return null;
    }

    @Override
    public InspectionPlanVO getInfo(String id) {
        return null;
    }

    @Override
    public void remove(String[] ids) {

    }

    @Override
    public void edit(InspectionPlanEditDTO param) {

    }

    @Override
    public void add(InspectionPlanAddDTO param) {

    }
}
