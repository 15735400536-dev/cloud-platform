package com.maxinhai.platform.service.model.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.model.WarehouseAddDTO;
import com.maxinhai.platform.dto.model.WarehouseEditDTO;
import com.maxinhai.platform.dto.model.WarehouseQueryDTO;
import com.maxinhai.platform.mapper.model.WarehouseMapper;
import com.maxinhai.platform.po.model.Warehouse;
import com.maxinhai.platform.service.model.WarehouseService;
import com.maxinhai.platform.vo.model.WarehouseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WarehouseServiceImpl extends ServiceImpl<WarehouseMapper, Warehouse> implements WarehouseService {

    @Resource
    private WarehouseMapper warehouseMapper;

    @Override
    public Page<WarehouseVO> searchByPage(WarehouseQueryDTO param) {
        Page<WarehouseVO> pageResult = warehouseMapper.selectJoinPage(param.getPage(), WarehouseVO.class,
                new MPJLambdaWrapper<Warehouse>()
                        .like(StrUtil.isNotBlank(param.getCode()), Warehouse::getCode, param.getCode())
                        .like(StrUtil.isNotBlank(param.getName()), Warehouse::getName, param.getName())
                        .orderByDesc(Warehouse::getCreateTime));
        return pageResult;
    }

    @Override
    public WarehouseVO getInfo(String id) {
        return warehouseMapper.selectJoinOne(WarehouseVO.class, new MPJLambdaWrapper<Warehouse>().eq(Warehouse::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        warehouseMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(WarehouseEditDTO param) {
        Warehouse warehouse = BeanUtil.toBean(param, Warehouse.class);
        warehouseMapper.updateById(warehouse);
    }

    @Override
    public void add(WarehouseAddDTO param) {
        Warehouse warehouse = BeanUtil.toBean(param, Warehouse.class);
        warehouseMapper.insert(warehouse);
    }
}
