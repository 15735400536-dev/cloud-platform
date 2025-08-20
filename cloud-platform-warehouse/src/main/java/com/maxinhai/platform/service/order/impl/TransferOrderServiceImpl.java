package com.maxinhai.platform.service.order.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.order.TransferOrderAddDTO;
import com.maxinhai.platform.dto.order.TransferOrderEditDTO;
import com.maxinhai.platform.dto.order.TransferOrderQueryDTO;
import com.maxinhai.platform.enums.OperateType;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.mapper.inventory.InventoryMapper;
import com.maxinhai.platform.mapper.order.TransferOrderDetailMapper;
import com.maxinhai.platform.mapper.order.TransferOrderMapper;
import com.maxinhai.platform.po.inventory.Inventory;
import com.maxinhai.platform.po.order.TransferOrder;
import com.maxinhai.platform.po.order.TransferOrderDetail;
import com.maxinhai.platform.service.inventory.InventoryFlowService;
import com.maxinhai.platform.service.inventory.InventoryService;
import com.maxinhai.platform.service.order.TransferOrderService;
import com.maxinhai.platform.vo.order.TransferOrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TransferOrderServiceImpl extends ServiceImpl<TransferOrderMapper, TransferOrder> implements TransferOrderService {

    @Resource
    private TransferOrderMapper transferOrderMapper;
    @Resource
    private TransferOrderDetailMapper transferOrderDetailMapper;
    @Resource
    private InventoryMapper inventoryMapper;
    @Resource
    private InventoryService inventoryService;
    @Resource
    private InventoryFlowService inventoryFlowService;

    @Override
    public Page<TransferOrderVO> searchByPage(TransferOrderQueryDTO param) {
        Page<TransferOrderVO> pageResult = transferOrderMapper.selectJoinPage(param.getPage(), TransferOrderVO.class,
                new MPJLambdaWrapper<TransferOrder>()
                        .like(StrUtil.isNotBlank(param.getTransferNo()), TransferOrder::getTransferNo, param.getTransferNo())
                        .eq(StrUtil.isNotBlank(param.getSourceWarehouseId()), TransferOrder::getSourceWarehouseId, param.getSourceWarehouseId())
                        .eq(StrUtil.isNotBlank(param.getTargetWarehouseId()), TransferOrder::getTargetWarehouseId, param.getTargetWarehouseId())
                        .orderByDesc(TransferOrder::getCreateTime));
        return pageResult;
    }

    @Override
    public TransferOrderVO getInfo(String id) {
        return transferOrderMapper.selectJoinOne(TransferOrderVO.class, new MPJLambdaWrapper<TransferOrder>().eq(StrUtil.isNotBlank(id), TransferOrder::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        transferOrderMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(TransferOrderEditDTO param) {
        TransferOrder order = BeanUtil.toBean(param, TransferOrder.class);
        transferOrderMapper.updateById(order);
    }

    @Override
    public void add(TransferOrderAddDTO param) {
        TransferOrder order = BeanUtil.toBean(param, TransferOrder.class);
        transferOrderMapper.insert(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transfer(String id) {
        // 查询移库单信息
        TransferOrder transferOrder = transferOrderMapper.selectOne(new LambdaQueryWrapper<TransferOrder>()
                .select(TransferOrder::getId, TransferOrder::getStatus, TransferOrder::getTransferNo)
                .eq(TransferOrder::getId, id));
        if (2 > transferOrder.getStatus()) {
            throw new BusinessException("移库单未审核通过！");
        }
        if (4 == transferOrder.getStatus()) {
            throw new BusinessException("移库单移库已完成！");
        }
        // 查询移库单明细
        List<TransferOrderDetail> orderDetailList = transferOrderDetailMapper.selectList(new LambdaQueryWrapper<TransferOrderDetail>()
                .eq(TransferOrderDetail::getTransferOrderId, id));
        // 查询库存明细
        List<String> locationIds = new ArrayList<>();
        List<String> materialIds = new ArrayList<>();
        for (TransferOrderDetail orderDetail : orderDetailList) {
            locationIds.add(orderDetail.getSourceLocationId());
            locationIds.add(orderDetail.getTargetLocationId());
            materialIds.add(orderDetail.getMaterialId());
        }
        Map<String, Inventory> inventoryMap = inventoryMapper.selectList(new LambdaQueryWrapper<Inventory>()
                        .in(Inventory::getMaterialId, materialIds)
                        .in(Inventory::getLocationId, locationIds)).stream()
                .collect(Collectors.toMap(inventory -> inventory.getLocationId() + "_" + inventory.getMaterialId(),
                        inventory -> inventory,
                        (existing, replacement) -> existing));
        // 移库
        List<Inventory> updateInventories = new ArrayList<>();
        for (TransferOrderDetail orderDetail : orderDetailList) {
            String sourceLocationId = orderDetail.getSourceLocationId();
            String targetLocationId = orderDetail.getTargetLocationId();
            String materialId = orderDetail.getMaterialId();
            BigDecimal actualQty = orderDetail.getActualQty();

            Inventory sourceInventory = inventoryMap.get(sourceLocationId + "_" + materialId);
            if (Objects.isNull(sourceInventory)) {
                throw new BusinessException("源货位库存明细不存在！");
            }
            Inventory targetInventory = inventoryMap.get(targetLocationId + "_" + materialId);
            if (Objects.isNull(targetInventory)) {
                targetInventory = new Inventory();
                targetInventory.setWarehouseId(orderDetail.getTargetWarehouseId());
                targetInventory.setAreaId(orderDetail.getTargetAreaId());
                targetInventory.setRackId(orderDetail.getTargetRackId());
                targetInventory.setLocationId(orderDetail.getTargetLocationId());
                targetInventory.setMaterialId(orderDetail.getMaterialId());
                targetInventory.setBatchNo(orderDetail.getBatchNo());
                targetInventory.setQty(orderDetail.getActualQty());
                targetInventory.setLockedQty(BigDecimal.ZERO);
                targetInventory.setAvailableQty(orderDetail.getActualQty());
                inventoryMapper.insert(targetInventory);
                throw new BusinessException("目标货位库存明细不存在！");
            }
            // 源库位库存-移库数量
            inventoryFlowService.createFlow(OperateType.RECEIPT, sourceInventory,
                    orderDetail.getActualQty(), sourceInventory.getQty(), sourceInventory.getQty().subtract(actualQty));
            sourceInventory.setQty(sourceInventory.getQty().subtract(actualQty));
            // 目标库位库存+移库数量
            inventoryFlowService.createFlow(OperateType.ISSUE, targetInventory,
                    orderDetail.getActualQty(), targetInventory.getQty(), targetInventory.getQty().add(actualQty));
            targetInventory.setQty(targetInventory.getQty().add(actualQty));

            updateInventories.add(sourceInventory);
            updateInventories.add(targetInventory);
        }
        inventoryService.updateBatchById(updateInventories);
    }
}
