package com.maxinhai.platform.service.model.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.model.WorkshopAddDTO;
import com.maxinhai.platform.dto.model.WorkshopEditDTO;
import com.maxinhai.platform.dto.model.WorkshopQueryDTO;
import com.maxinhai.platform.mapper.model.WorkshopMapper;
import com.maxinhai.platform.po.model.Factory;
import com.maxinhai.platform.po.model.Workshop;
import com.maxinhai.platform.service.model.WorkshopService;
import com.maxinhai.platform.vo.model.WorkshopVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WorkshopServiceImpl extends ServiceImpl<WorkshopMapper, Workshop> implements WorkshopService {

    @Resource
    private WorkshopMapper workshopMapper;

    @Override
    public Page<WorkshopVO> searchByPage(WorkshopQueryDTO param) {
        Page<WorkshopVO> pageResult = workshopMapper.selectJoinPage(param.getPage(), WorkshopVO.class,
                new MPJLambdaWrapper<Workshop>()
                        .innerJoin(Factory.class, Factory::getId, Workshop::getFactoryId)
                        // 查询条件
                        .like(StrUtil.isNotBlank(param.getCode()), Workshop::getCode, param.getCode())
                        .like(StrUtil.isNotBlank(param.getName()), Workshop::getName, param.getName())
                        // 字段别名
                        .selectAll(Workshop.class)
                        .selectAs(Factory::getCode, WorkshopVO::getFactoryCode)
                        .selectAs(Factory::getName, WorkshopVO::getFactoryName)
                        // 排序
                        .orderByDesc(Workshop::getCreateTime));
        return pageResult;
    }

    @Override
    public WorkshopVO getInfo(String id) {
        return workshopMapper.selectJoinOne(WorkshopVO.class,
                new MPJLambdaWrapper<Workshop>()
                        .innerJoin(Factory.class, Factory::getId, Workshop::getFactoryId)
                        // 查询条件
                        .eq(StrUtil.isNotBlank(id), Workshop::getId, id)
                        // 字段别名
                        .selectAll(Workshop.class)
                        .selectAs(Factory::getCode, WorkshopVO::getFactoryCode)
                        .selectAs(Factory::getName, WorkshopVO::getFactoryName));
    }

    @Override
    public void remove(String[] ids) {
        workshopMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(WorkshopEditDTO param) {
        Workshop user = BeanUtil.toBean(param, Workshop.class);
        workshopMapper.updateById(user);
    }

    @Override
    public void add(WorkshopAddDTO param) {
        Workshop user = BeanUtil.toBean(param, Workshop.class);
        workshopMapper.insert(user);
    }
}
