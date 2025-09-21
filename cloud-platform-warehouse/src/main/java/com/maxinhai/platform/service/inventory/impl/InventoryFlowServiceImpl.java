package com.maxinhai.platform.service.inventory.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.inventory.InventoryFlowQueryDTO;
import com.maxinhai.platform.enums.OperateType;
import com.maxinhai.platform.mapper.inventory.InventoryFlowMapper;
import com.maxinhai.platform.po.Material;
import com.maxinhai.platform.po.inventory.Inventory;
import com.maxinhai.platform.po.inventory.InventoryFlow;
import com.maxinhai.platform.po.model.Warehouse;
import com.maxinhai.platform.po.model.WarehouseArea;
import com.maxinhai.platform.po.model.WarehouseLocation;
import com.maxinhai.platform.po.model.WarehouseRack;
import com.maxinhai.platform.po.order.IssueOrderDetail;
import com.maxinhai.platform.po.order.ReceiptOrderDetail;
import com.maxinhai.platform.po.order.TransferOrderDetail;
import com.maxinhai.platform.service.inventory.InventoryFlowService;
import com.maxinhai.platform.vo.inventory.InventoryFlowVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName：InventoryFlowServiceImpl
 * @Author: XinHai.Ma
 * @Date: 2025/8/20 20:31
 * @Description: 库存流水业务层
 */
@Slf4j
@Service
public class InventoryFlowServiceImpl extends ServiceImpl<InventoryFlowMapper, InventoryFlow> implements InventoryFlowService {

    @Resource
    private InventoryFlowMapper inventoryFlowMapper;

    @Override
    public Page<InventoryFlowVO> searchByPage(InventoryFlowQueryDTO param) {
        Page<InventoryFlowVO> pageResult = inventoryFlowMapper.selectJoinPage(param.getPage(), InventoryFlowVO.class,
                new MPJLambdaWrapper<InventoryFlow>()
                        .leftJoin(Warehouse.class, Warehouse::getId, InventoryFlow::getWarehouseId)
                        .leftJoin(WarehouseArea.class, WarehouseArea::getId, InventoryFlow::getAreaId)
                        .leftJoin(WarehouseRack.class, WarehouseRack::getId, InventoryFlow::getRackId)
                        .leftJoin(WarehouseLocation.class, WarehouseLocation::getId, InventoryFlow::getLocationId)
                        .leftJoin(Material.class, Material::getId, InventoryFlow::getMaterialId)
                        // 查询条件
                        .eq(StrUtil.isNotBlank(param.getWarehouseId()), InventoryFlow::getWarehouseId, param.getWarehouseId())
                        .eq(StrUtil.isNotBlank(param.getAreaId()), InventoryFlow::getAreaId, param.getAreaId())
                        .eq(StrUtil.isNotBlank(param.getRackId()), InventoryFlow::getRackId, param.getRackId())
                        .eq(StrUtil.isNotBlank(param.getLocationId()), InventoryFlow::getLocationId, param.getLocationId())
                        // 字段别名
                        .selectAll(InventoryFlow.class)
                        .selectAs(Warehouse::getCode, InventoryFlowVO::getWarehouseCode)
                        .selectAs(Warehouse::getName, InventoryFlowVO::getWarehouseName)
                        .selectAs(WarehouseArea::getCode, InventoryFlowVO::getAreaCode)
                        .selectAs(WarehouseArea::getName, InventoryFlowVO::getAreaName)
                        .selectAs(WarehouseRack::getCode, InventoryFlowVO::getRackCode)
                        .selectAs(WarehouseRack::getName, InventoryFlowVO::getRackName)
                        .selectAs(WarehouseLocation::getCode, InventoryFlowVO::getLocationCode)
                        .selectAs(WarehouseLocation::getName, InventoryFlowVO::getLocationName)
                        .selectAs(Material::getCode, InventoryFlowVO::getMaterialCode)
                        .selectAs(Material::getName, InventoryFlowVO::getMaterialName)
                        // 排序
                        .orderByDesc(InventoryFlow::getCreateTime));
        return pageResult;
    }

    @Override
    public InventoryFlowVO getInfo(String id) {
        return inventoryFlowMapper.selectJoinOne(InventoryFlowVO.class,
                new MPJLambdaWrapper<InventoryFlow>()
                        .leftJoin(Warehouse.class, Warehouse::getId, InventoryFlow::getWarehouseId)
                        .leftJoin(WarehouseArea.class, WarehouseArea::getId, InventoryFlow::getAreaId)
                        .leftJoin(WarehouseRack.class, WarehouseRack::getId, InventoryFlow::getRackId)
                        .leftJoin(WarehouseLocation.class, WarehouseLocation::getId, InventoryFlow::getLocationId)
                        .leftJoin(Material.class, Material::getId, InventoryFlow::getMaterialId)
                        // 查询条件
                        .eq(StrUtil.isNotBlank(id), InventoryFlow::getId, id)
                        // 字段别名
                        .selectAll(InventoryFlow.class)
                        .selectAs(Warehouse::getCode, InventoryFlowVO::getWarehouseCode)
                        .selectAs(Warehouse::getName, InventoryFlowVO::getWarehouseName)
                        .selectAs(WarehouseArea::getCode, InventoryFlowVO::getAreaCode)
                        .selectAs(WarehouseArea::getName, InventoryFlowVO::getAreaName)
                        .selectAs(WarehouseRack::getCode, InventoryFlowVO::getRackCode)
                        .selectAs(WarehouseRack::getName, InventoryFlowVO::getRackName)
                        .selectAs(WarehouseLocation::getCode, InventoryFlowVO::getLocationCode)
                        .selectAs(WarehouseLocation::getName, InventoryFlowVO::getLocationName)
                        .selectAs(Material::getCode, InventoryFlowVO::getMaterialCode)
                        .selectAs(Material::getName, InventoryFlowVO::getMaterialName));
    }

    @Override
    public void createFlow(TransferOrderDetail orderDetail, BigDecimal beforeQty, BigDecimal afterQt) {
        InventoryFlow inventoryFlow = new InventoryFlow();
        inventoryFlow.setOrderCode(null);
        inventoryFlow.setOrderType(null);
        inventoryFlow.setWarehouseId(orderDetail.getSourceWarehouseId());
        inventoryFlow.setAreaId(orderDetail.getSourceAreaId());
        inventoryFlow.setRackId(orderDetail.getSourceRackId());
        inventoryFlow.setLocationId(orderDetail.getSourceLocationId());
        inventoryFlow.setMaterialId(orderDetail.getMaterialId());
        inventoryFlow.setBatchNo(orderDetail.getBatchNo());
        inventoryFlow.setBeforeQty(beforeQty);
        inventoryFlow.setAfterQt(afterQt);
        inventoryFlow.setChangeQty(orderDetail.getActualQty());
        inventoryFlow.setOperateType(OperateType.INVENTORY_ADJUSTMENT);
        inventoryFlow.setOperatorId("anonymous");
        inventoryFlow.setOperateTime(new Date());
        inventoryFlowMapper.insert(inventoryFlow);
    }

    @Override
    public void createFlow(ReceiptOrderDetail orderDetail, BigDecimal beforeQty, BigDecimal afterQt) {
        InventoryFlow inventoryFlow = new InventoryFlow();
        inventoryFlow.setOrderCode(null);
        inventoryFlow.setOrderType(null);
//        inventoryFlow.setWarehouseId(orderDetail.getSourceWarehouseId());
//        inventoryFlow.setAreaId(orderDetail.getSourceAreaId());
//        inventoryFlow.setRackId(orderDetail.getSourceRackId());
        inventoryFlow.setLocationId(orderDetail.getLocationId());
        inventoryFlow.setMaterialId(orderDetail.getMaterialId());
        inventoryFlow.setBatchNo(orderDetail.getBatchNo());
        inventoryFlow.setBeforeQty(beforeQty);
        inventoryFlow.setAfterQt(afterQt);
        inventoryFlow.setChangeQty(orderDetail.getActualQty());
        inventoryFlow.setOperateType(OperateType.RECEIPT);
        inventoryFlow.setOperatorId("anonymous");
        inventoryFlow.setOperateTime(new Date());
        inventoryFlowMapper.insert(inventoryFlow);
    }

    @Override
    public void createFlow(IssueOrderDetail orderDetail, BigDecimal beforeQty, BigDecimal afterQt) {
        InventoryFlow inventoryFlow = new InventoryFlow();
        inventoryFlow.setOrderCode(null);
        inventoryFlow.setOrderType(null);
//        inventoryFlow.setWarehouseId(orderDetail.getSourceWarehouseId());
//        inventoryFlow.setAreaId(orderDetail.getSourceAreaId());
//        inventoryFlow.setRackId(orderDetail.getSourceRackId());
        inventoryFlow.setLocationId(orderDetail.getLocationId());
        inventoryFlow.setMaterialId(orderDetail.getMaterialId());
        inventoryFlow.setBatchNo(orderDetail.getBatchNo());
        inventoryFlow.setBeforeQty(beforeQty);
        inventoryFlow.setAfterQt(afterQt);
        inventoryFlow.setChangeQty(orderDetail.getActualQty());
        inventoryFlow.setOperateType(OperateType.ISSUE);
        inventoryFlow.setOperatorId("anonymous");
        inventoryFlow.setOperateTime(new Date());
        inventoryFlowMapper.insert(inventoryFlow);
    }

    @Override
    public void createFlow(OperateType operateType, Inventory inventory, BigDecimal changeQty, BigDecimal beforeQty, BigDecimal afterQt) {
        InventoryFlow inventoryFlow = new InventoryFlow();
        inventoryFlow.setOrderCode(null);
        inventoryFlow.setOrderType(null);
        inventoryFlow.setWarehouseId(inventory.getWarehouseId());
        inventoryFlow.setAreaId(inventory.getAreaId());
        inventoryFlow.setRackId(inventory.getRackId());
        inventoryFlow.setLocationId(inventory.getLocationId());
        inventoryFlow.setMaterialId(inventory.getMaterialId());
        inventoryFlow.setBatchNo(inventory.getBatchNo());
        inventoryFlow.setBeforeQty(beforeQty);
        inventoryFlow.setAfterQt(afterQt);
        inventoryFlow.setChangeQty(changeQty);
        inventoryFlow.setOperateType(operateType);
        inventoryFlow.setOperatorId("anonymous");
        inventoryFlow.setOperateTime(new Date());
        inventoryFlowMapper.insert(inventoryFlow);
    }
}
