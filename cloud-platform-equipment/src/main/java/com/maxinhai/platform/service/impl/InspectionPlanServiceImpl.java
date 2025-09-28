package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.InspectionPlanAddDTO;
import com.maxinhai.platform.dto.InspectionPlanEditDTO;
import com.maxinhai.platform.dto.InspectionPlanQueryDTO;
import com.maxinhai.platform.mapper.InspectionPlanMapper;
import com.maxinhai.platform.po.InspectionPlan;
import com.maxinhai.platform.service.InspectionPlanService;
import com.maxinhai.platform.vo.InspectionPlanVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
public class InspectionPlanServiceImpl extends ServiceImpl<InspectionPlanMapper, InspectionPlan> implements InspectionPlanService {

    @Resource
    private InspectionPlanMapper inspectionPlanMapper;

    @Override
    public Page<InspectionPlanVO> searchByPage(InspectionPlanQueryDTO param) {
        return inspectionPlanMapper.selectJoinPage(param.getPage(), InspectionPlanVO.class,
                new MPJLambdaWrapper<InspectionPlan>()
                        .like(StrUtil.isNotBlank(param.getPlanCode()), InspectionPlan::getPlanCode, param.getPlanCode())
                        .orderByDesc(InspectionPlan::getCreateTime));
    }

    @Override
    public InspectionPlanVO getInfo(String id) {
        return inspectionPlanMapper.selectJoinOne(InspectionPlanVO.class,
                new MPJLambdaWrapper<InspectionPlan>()
                        .eq(StrUtil.isNotBlank(id), InspectionPlan::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        inspectionPlanMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(InspectionPlanEditDTO param) {
        InspectionPlan plan = BeanUtil.toBean(param, InspectionPlan.class);
        inspectionPlanMapper.updateById(plan);
    }

    @Override
    public void add(InspectionPlanAddDTO param) {
        InspectionPlan plan = BeanUtil.toBean(param, InspectionPlan.class);
        inspectionPlanMapper.insert(plan);
    }
}
