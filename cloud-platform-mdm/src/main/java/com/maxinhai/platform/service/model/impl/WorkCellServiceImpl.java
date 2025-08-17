package com.maxinhai.platform.service.model.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.model.WorkCellAddDTO;
import com.maxinhai.platform.dto.model.WorkCellEditDTO;
import com.maxinhai.platform.dto.model.WorkCellQueryDTO;
import com.maxinhai.platform.mapper.model.WorkCellMapper;
import com.maxinhai.platform.po.model.ProductionLine;
import com.maxinhai.platform.po.model.WorkCell;
import com.maxinhai.platform.po.model.WorkCenter;
import com.maxinhai.platform.service.model.WorkCellService;
import com.maxinhai.platform.vo.model.WorkCellVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WorkCellServiceImpl extends ServiceImpl<WorkCellMapper, WorkCell> implements WorkCellService {

    @Resource
    private WorkCellMapper workCellMapper;

    @Override
    public Page<WorkCellVO> searchByPage(WorkCellQueryDTO param) {
        Page<WorkCellVO> pageResult = workCellMapper.selectJoinPage(param.getPage(), WorkCellVO.class,
                new MPJLambdaWrapper<WorkCell>()
                        .leftJoin(WorkCenter.class, WorkCenter::getId, WorkCell::getWorkCenterId)
                        .leftJoin(ProductionLine.class, ProductionLine::getId, WorkCell::getProductionLineId)
                        // 查询条件
                        .like(StrUtil.isNotBlank(param.getCode()), WorkCell::getCode, param.getCode())
                        .like(StrUtil.isNotBlank(param.getName()), WorkCell::getName, param.getName())
                        // 字段别名
                        .selectAll(WorkCell.class)
                        .selectAs(WorkCenter::getCode, WorkCellVO::getWorkCenterCode)
                        .selectAs(WorkCenter::getName, WorkCellVO::getWorkCenterName)
                        .selectAs(ProductionLine::getCode, WorkCellVO::getProductionLineCode)
                        .selectAs(ProductionLine::getName, WorkCellVO::getProductionLineName)
                        // 排序
                        .orderByDesc(WorkCell::getCreateTime));
        return pageResult;
    }

    @Override
    public WorkCellVO getInfo(String id) {
        return workCellMapper.selectJoinOne(WorkCellVO.class,
                new MPJLambdaWrapper<WorkCell>()
                        .leftJoin(WorkCenter.class, WorkCenter::getId, WorkCell::getWorkCenterId)
                        .leftJoin(ProductionLine.class, ProductionLine::getId, WorkCell::getProductionLineId)
                        // 查询条件
                        .eq(StrUtil.isNotBlank(id), WorkCell::getId, id)
                        // 字段别名
                        .selectAll(WorkCell.class)
                        .selectAs(WorkCenter::getCode, WorkCellVO::getWorkCenterCode)
                        .selectAs(WorkCenter::getName, WorkCellVO::getWorkCenterName)
                        .selectAs(ProductionLine::getCode, WorkCellVO::getProductionLineCode)
                        .selectAs(ProductionLine::getName, WorkCellVO::getProductionLineName));
    }

    @Override
    public void remove(String[] ids) {
        workCellMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(WorkCellEditDTO param) {
        WorkCell user = BeanUtil.toBean(param, WorkCell.class);
        workCellMapper.updateById(user);
    }

    @Override
    public void add(WorkCellAddDTO param) {
        WorkCell user = BeanUtil.toBean(param, WorkCell.class);
        workCellMapper.insert(user);
    }
}
