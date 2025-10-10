package com.maxinhai.platform.service.model.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.WorkCenterAddDTO;
import com.maxinhai.platform.dto.WorkCenterEditDTO;
import com.maxinhai.platform.dto.WorkCenterQueryDTO;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.mapper.WorkCenterMapper;
import com.maxinhai.platform.po.model.WorkCenter;
import com.maxinhai.platform.po.model.Workshop;
import com.maxinhai.platform.service.CommonCodeCheckService;
import com.maxinhai.platform.service.model.WorkCenterService;
import com.maxinhai.platform.vo.WorkCenterVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WorkCenterServiceImpl extends ServiceImpl<WorkCenterMapper, WorkCenter> implements WorkCenterService {

    @Resource
    private WorkCenterMapper workCenterMapper;
    @Resource
    private CommonCodeCheckService commonCodeCheckService;

    @Override
    public Page<WorkCenterVO> searchByPage(WorkCenterQueryDTO param) {
        return workCenterMapper.selectJoinPage(param.getPage(), WorkCenterVO.class,
                new MPJLambdaWrapper<WorkCenter>()
                        .innerJoin(Workshop.class, Workshop::getId, WorkCenter::getWorkshopId)
                        // 查询条件
                        .like(StrUtil.isNotBlank(param.getCode()), WorkCenter::getCode, param.getCode())
                        .like(StrUtil.isNotBlank(param.getName()), WorkCenter::getName, param.getName())
                        // 字段别名
                        .selectAll(WorkCenter.class)
                        .selectAs(Workshop::getCode, WorkCenterVO::getWorkshopCode)
                        .selectAs(Workshop::getName, WorkCenterVO::getWorkshopName)
                        // 排序
                        .orderByDesc(WorkCenter::getCreateTime));
    }

    @Override
    public WorkCenterVO getInfo(String id) {
        return workCenterMapper.selectJoinOne(WorkCenterVO.class,
                new MPJLambdaWrapper<WorkCenter>()
                        .innerJoin(Workshop.class, Workshop::getId, WorkCenter::getWorkshopId)
                        // 查询条件
                        .eq(WorkCenter::getId, id)
                        // 字段别名
                        .selectAll(WorkCenter.class)
                        .selectAs(Workshop::getCode, WorkCenterVO::getWorkshopCode)
                        .selectAs(Workshop::getName, WorkCenterVO::getWorkshopName));
    }

    @Override
    public void remove(String[] ids) {
        workCenterMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(WorkCenterEditDTO param) {
        WorkCenter workCenter = BeanUtil.toBean(param, WorkCenter.class);
        workCenterMapper.updateById(workCenter);
    }

    @Override
    public void add(WorkCenterAddDTO param) {
        boolean unique = commonCodeCheckService.isCodeUnique(WorkCenter.class, WorkCenter::getCode, param.getCode());
        if (!unique) {
            throw new BusinessException("工作中心【" + param.getCode() + "】已存在!");
        }
        WorkCenter workCenter = BeanUtil.toBean(param, WorkCenter.class);
        workCenterMapper.updateById(workCenter);
    }
}
