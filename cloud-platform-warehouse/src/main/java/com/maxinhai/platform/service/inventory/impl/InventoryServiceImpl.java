package com.maxinhai.platform.service.inventory.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.inventory.InventoryQueryDTO;
import com.maxinhai.platform.po.Material;
import com.maxinhai.platform.mapper.inventory.InventoryMapper;
import com.maxinhai.platform.po.inventory.Inventory;
import com.maxinhai.platform.po.model.Warehouse;
import com.maxinhai.platform.po.model.WarehouseArea;
import com.maxinhai.platform.po.model.WarehouseLocation;
import com.maxinhai.platform.po.model.WarehouseRack;
import com.maxinhai.platform.service.inventory.InventoryService;
import com.maxinhai.platform.vo.inventory.InventoryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory> implements InventoryService {

    @Resource
    private InventoryMapper inventoryMapper;

    @Override
    public Page<InventoryVO> searchByPage(InventoryQueryDTO param) {
        Page<InventoryVO> pageResult = inventoryMapper.selectJoinPage(param.getPage(), InventoryVO.class,
                new MPJLambdaWrapper<Inventory>()
                        .innerJoin(Warehouse.class, Warehouse::getId, Inventory::getWarehouseId)
                        .innerJoin(WarehouseArea.class, WarehouseArea::getId, Inventory::getAreaId)
                        .innerJoin(WarehouseRack.class, WarehouseRack::getId, Inventory::getRackId)
                        .innerJoin(WarehouseLocation.class, WarehouseLocation::getId, Inventory::getLocationId)
                        .innerJoin(Material.class, Material::getId, Inventory::getMaterialId)
                        // 查询条件
                        .like(StrUtil.isNotBlank(param.getWarehouseId()), Inventory::getWarehouseId, param.getWarehouseId())
                        .like(StrUtil.isNotBlank(param.getAreaId()), Inventory::getAreaId, param.getAreaId())
                        .like(StrUtil.isNotBlank(param.getRackId()), Inventory::getRackId, param.getRackId())
                        .like(StrUtil.isNotBlank(param.getLocationId()), Inventory::getLocationId, param.getLocationId())
                        .like(StrUtil.isNotBlank(param.getMaterialId()), Inventory::getMaterialId, param.getMaterialId())
                        // 字段别名
                        .selectAll(Inventory.class)
                        .selectAs(Warehouse::getCode, InventoryVO::getWarehouseCode)
                        .selectAs(Warehouse::getName, InventoryVO::getWarehouseName)
                        .selectAs(WarehouseArea::getCode, InventoryVO::getAreaCode)
                        .selectAs(WarehouseArea::getName, InventoryVO::getAreaName)
                        .selectAs(WarehouseRack::getCode, InventoryVO::getRackCode)
                        .selectAs(WarehouseRack::getName, InventoryVO::getRackName)
                        .selectAs(WarehouseLocation::getCode, InventoryVO::getLocationCode)
                        .selectAs(WarehouseLocation::getName, InventoryVO::getLocationName)
                        .selectAs(Material::getCode, InventoryVO::getMaterialCode)
                        .selectAs(Material::getName, InventoryVO::getMaterialName)
                        // 排序
                        .orderByDesc(Inventory::getCreateTime));
        return pageResult;
    }

    @Override
    public InventoryVO getInfo(String id) {
        return inventoryMapper.selectJoinOne(InventoryVO.class, new MPJLambdaWrapper<Inventory>()
                .innerJoin(Warehouse.class, Warehouse::getId, Inventory::getWarehouseId)
                .innerJoin(WarehouseArea.class, WarehouseArea::getId, Inventory::getAreaId)
                .innerJoin(WarehouseRack.class, WarehouseRack::getId, Inventory::getRackId)
                .innerJoin(WarehouseLocation.class, WarehouseLocation::getId, Inventory::getLocationId)
                .innerJoin(Material.class, Material::getId, Inventory::getMaterialId)
                // 字段别名
                .selectAll(Inventory.class)
                .selectAs(Warehouse::getCode, InventoryVO::getWarehouseCode)
                .selectAs(Warehouse::getName, InventoryVO::getWarehouseName)
                .selectAs(WarehouseArea::getCode, InventoryVO::getAreaCode)
                .selectAs(WarehouseArea::getName, InventoryVO::getAreaName)
                .selectAs(WarehouseRack::getCode, InventoryVO::getRackCode)
                .selectAs(WarehouseRack::getName, InventoryVO::getRackName)
                .selectAs(WarehouseLocation::getCode, InventoryVO::getLocationCode)
                .selectAs(WarehouseLocation::getName, InventoryVO::getLocationName)
                .selectAs(Material::getCode, InventoryVO::getMaterialCode)
                .selectAs(Material::getName, InventoryVO::getMaterialName)
                // 查询条件
                .eq(Inventory::getId, id));
    }
}
