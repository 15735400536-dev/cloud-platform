package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.MaintenancePlanAddDTO;
import com.maxinhai.platform.dto.MaintenancePlanEditDTO;
import com.maxinhai.platform.dto.MaintenancePlanQueryDTO;
import com.maxinhai.platform.mapper.MaintenancePlanMapper;
import com.maxinhai.platform.po.MaintenancePlan;
import com.maxinhai.platform.service.MaintenancePlanService;
import com.maxinhai.platform.vo.MaintenancePlanVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MaintenancePlanServiceImpl extends ServiceImpl<MaintenancePlanMapper, MaintenancePlan>
        implements MaintenancePlanService {

    @Resource
    private MaintenancePlanMapper maintenancePlanMapper;

    @Override
    public Page<MaintenancePlanVO> searchByPage(MaintenancePlanQueryDTO param) {
        return maintenancePlanMapper.selectJoinPage(param.getPage(), MaintenancePlanVO.class,
                new MPJLambdaWrapper<MaintenancePlan>()
                        .like(StrUtil.isNotBlank(param.getPlanCode()), MaintenancePlan::getPlanCode, param.getPlanCode())
                        .orderByDesc(MaintenancePlan::getCreateTime));
    }

    @Override
    public MaintenancePlanVO getInfo(String id) {
        return maintenancePlanMapper.selectJoinOne(MaintenancePlanVO.class,
                new MPJLambdaWrapper<MaintenancePlan>()
                        .eq(StrUtil.isNotBlank(id), MaintenancePlan::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        maintenancePlanMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(MaintenancePlanEditDTO param) {
        MaintenancePlan plan = BeanUtil.toBean(param, MaintenancePlan.class);
        maintenancePlanMapper.updateById(plan);
    }

    @Override
    public void add(MaintenancePlanAddDTO param) {
        MaintenancePlan plan = BeanUtil.toBean(param, MaintenancePlan.class);
        maintenancePlanMapper.insert(plan);
    }
}
