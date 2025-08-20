package com.maxinhai.platform.service.inventory.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.maxinhai.platform.enums.OperateType;
import com.maxinhai.platform.mapper.inventory.InventoryFlowMapper;
import com.maxinhai.platform.po.inventory.Inventory;
import com.maxinhai.platform.po.inventory.InventoryFlow;
import com.maxinhai.platform.po.order.IssueOrderDetail;
import com.maxinhai.platform.po.order.ReceiptOrderDetail;
import com.maxinhai.platform.po.order.TransferOrderDetail;
import com.maxinhai.platform.service.inventory.InventoryFlowService;
import com.maxinhai.platform.utils.LoginUserContext;
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
        inventoryFlow.setOperatorId(LoginUserContext.getItemValue("userId"));
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
        inventoryFlow.setOperatorId(LoginUserContext.getItemValue("userId"));
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
        inventoryFlow.setOperatorId(LoginUserContext.getItemValue("userId"));
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
        inventoryFlow.setOperatorId(LoginUserContext.getItemValue("userId"));
        inventoryFlow.setOperateTime(new Date());
        inventoryFlowMapper.insert(inventoryFlow);
    }
}
