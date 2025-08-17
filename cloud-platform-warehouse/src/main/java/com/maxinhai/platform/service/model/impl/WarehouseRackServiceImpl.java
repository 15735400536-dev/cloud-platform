package com.maxinhai.platform.service.model.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.model.WarehouseRackAddDTO;
import com.maxinhai.platform.dto.model.WarehouseRackEditDTO;
import com.maxinhai.platform.dto.model.WarehouseRackQueryDTO;
import com.maxinhai.platform.mapper.model.WarehouseRackMapper;
import com.maxinhai.platform.po.model.Warehouse;
import com.maxinhai.platform.po.model.WarehouseArea;
import com.maxinhai.platform.po.model.WarehouseLocation;
import com.maxinhai.platform.po.model.WarehouseRack;
import com.maxinhai.platform.service.model.WarehouseRackService;
import com.maxinhai.platform.vo.model.WarehouseRackVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WarehouseRackServiceImpl extends ServiceImpl<WarehouseRackMapper, WarehouseRack> implements WarehouseRackService {
    
    @Resource
    private WarehouseRackMapper rackMapper;
    
    @Override
    public Page<WarehouseRackVO> searchByPage(WarehouseRackQueryDTO param) {
        Page<WarehouseRackVO> pageResult = rackMapper.selectJoinPage(param.getPage(), WarehouseRackVO.class,
                new MPJLambdaWrapper<WarehouseRack>()
                        .innerJoin(Warehouse.class, Warehouse::getId, WarehouseLocation::getWarehouseId)
                        .innerJoin(WarehouseArea.class, WarehouseArea::getId, WarehouseLocation::getAreaId)
                        // 查询条件
                        .like(StrUtil.isNotBlank(param.getCode()), WarehouseRack::getCode, param.getCode())
                        .like(StrUtil.isNotBlank(param.getName()), WarehouseRack::getName, param.getName())
                        // 字段别名
                        .selectAll(WarehouseRack.class)
                        .selectAs(Warehouse::getCode, WarehouseRackVO::getWarehouseCode)
                        .selectAs(Warehouse::getName, WarehouseRackVO::getWarehouseName)
                        .selectAs(WarehouseArea::getCode, WarehouseRackVO::getAreaCode)
                        .selectAs(WarehouseArea::getName, WarehouseRackVO::getAreaName)
                        // 排序
                        .orderByDesc(WarehouseRack::getCreateTime));
        return pageResult;
    }

    @Override
    public WarehouseRackVO getInfo(String id) {
        return rackMapper.selectJoinOne(WarehouseRackVO.class, new MPJLambdaWrapper<WarehouseRack>()
                .innerJoin(Warehouse.class, Warehouse::getId, WarehouseLocation::getWarehouseId)
                .innerJoin(WarehouseArea.class, WarehouseArea::getId, WarehouseLocation::getAreaId)
                // 字段别名
                .selectAll(WarehouseRack.class)
                .selectAs(Warehouse::getCode, WarehouseRackVO::getWarehouseCode)
                .selectAs(Warehouse::getName, WarehouseRackVO::getWarehouseName)
                .selectAs(WarehouseArea::getCode, WarehouseRackVO::getAreaCode)
                .selectAs(WarehouseArea::getName, WarehouseRackVO::getAreaName)
                // 查询条件
                .eq(WarehouseRack::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        rackMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(WarehouseRackEditDTO param) {
        WarehouseRack rack = BeanUtil.toBean(param, WarehouseRack.class);
        rackMapper.updateById(rack);
    }

    @Override
    public void add(WarehouseRackAddDTO param) {
        WarehouseRack rack = BeanUtil.toBean(param, WarehouseRack.class);
        rackMapper.insert(rack);
    }
}
