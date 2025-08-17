package com.maxinhai.platform.service.model.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.model.WarehouseLocationAddDTO;
import com.maxinhai.platform.dto.model.WarehouseLocationEditDTO;
import com.maxinhai.platform.dto.model.WarehouseLocationQueryDTO;
import com.maxinhai.platform.mapper.model.WarehouseLocationMapper;
import com.maxinhai.platform.po.model.Warehouse;
import com.maxinhai.platform.po.model.WarehouseArea;
import com.maxinhai.platform.po.model.WarehouseLocation;
import com.maxinhai.platform.po.model.WarehouseRack;
import com.maxinhai.platform.service.model.WarehouseLocationService;
import com.maxinhai.platform.vo.model.WarehouseLocationVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WarehouseLocationServiceImpl extends ServiceImpl<WarehouseLocationMapper, WarehouseLocation>
        implements WarehouseLocationService {
    
    @Resource
    private WarehouseLocationMapper locationMapper;
    
    @Override
    public Page<WarehouseLocationVO> searchByPage(WarehouseLocationQueryDTO param) {
        Page<WarehouseLocationVO> pageResult = locationMapper.selectJoinPage(param.getPage(), WarehouseLocationVO.class,
                new MPJLambdaWrapper<WarehouseLocation>()
                        .innerJoin(Warehouse.class, Warehouse::getId, WarehouseLocation::getWarehouseId)
                        .innerJoin(WarehouseArea.class, WarehouseArea::getId, WarehouseLocation::getAreaId)
                        .innerJoin(WarehouseRack.class, WarehouseRack::getId, WarehouseLocation::getRackId)
                        // 查询条件
                        .like(StrUtil.isNotBlank(param.getCode()), WarehouseLocation::getCode, param.getCode())
                        .like(StrUtil.isNotBlank(param.getName()), WarehouseLocation::getName, param.getName())
                        // 字段别名
                        .selectAll(WarehouseLocation.class)
                        .selectAs(Warehouse::getCode, WarehouseLocationVO::getWarehouseCode)
                        .selectAs(Warehouse::getName, WarehouseLocationVO::getWarehouseName)
                        .selectAs(WarehouseArea::getCode, WarehouseLocationVO::getAreaCode)
                        .selectAs(WarehouseArea::getName, WarehouseLocationVO::getAreaName)
                        .selectAs(WarehouseRack::getCode, WarehouseLocationVO::getRackCode)
                        .selectAs(WarehouseRack::getName, WarehouseLocationVO::getRackName)
                        // 排序
                        .orderByDesc(WarehouseLocation::getCreateTime));
        return pageResult;
    }

    @Override
    public WarehouseLocationVO getInfo(String id) {
        return locationMapper.selectJoinOne(WarehouseLocationVO.class, new MPJLambdaWrapper<WarehouseLocation>()
                .innerJoin(Warehouse.class, Warehouse::getId, WarehouseLocation::getWarehouseId)
                .innerJoin(WarehouseArea.class, WarehouseArea::getId, WarehouseLocation::getAreaId)
                .innerJoin(WarehouseRack.class, WarehouseRack::getId, WarehouseLocation::getRackId)
                // 字段别名
                .selectAll(WarehouseLocation.class)
                .selectAs(Warehouse::getCode, WarehouseLocationVO::getWarehouseCode)
                .selectAs(Warehouse::getName, WarehouseLocationVO::getWarehouseName)
                .selectAs(WarehouseArea::getCode, WarehouseLocationVO::getAreaCode)
                .selectAs(WarehouseArea::getName, WarehouseLocationVO::getAreaName)
                .selectAs(WarehouseRack::getCode, WarehouseLocationVO::getRackCode)
                .selectAs(WarehouseRack::getName, WarehouseLocationVO::getRackName)
                // 查询条件
                .eq(WarehouseLocation::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        locationMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(WarehouseLocationEditDTO param) {
        WarehouseLocation location = BeanUtil.toBean(param, WarehouseLocation.class);
        locationMapper.updateById(location);
    }

    @Override
    public void add(WarehouseLocationAddDTO param) {
        WarehouseLocation location = BeanUtil.toBean(param, WarehouseLocation.class);
        locationMapper.insert(location);
    }
}
