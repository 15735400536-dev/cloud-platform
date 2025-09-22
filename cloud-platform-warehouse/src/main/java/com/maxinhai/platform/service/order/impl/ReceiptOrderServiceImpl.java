package com.maxinhai.platform.service.order.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.order.ReceiptOrderAddDTO;
import com.maxinhai.platform.dto.order.ReceiptOrderEditDTO;
import com.maxinhai.platform.dto.order.ReceiptOrderQueryDTO;
import com.maxinhai.platform.enums.OperateType;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.feign.SystemFeignClient;
import com.maxinhai.platform.mapper.inventory.InventoryMapper;
import com.maxinhai.platform.mapper.model.WarehouseLocationMapper;
import com.maxinhai.platform.mapper.order.ReceiptOrderDetailMapper;
import com.maxinhai.platform.mapper.order.ReceiptOrderMapper;
import com.maxinhai.platform.po.inventory.Inventory;
import com.maxinhai.platform.po.model.WarehouseLocation;
import com.maxinhai.platform.po.order.ReceiptOrder;
import com.maxinhai.platform.po.order.ReceiptOrderDetail;
import com.maxinhai.platform.service.inventory.InventoryFlowService;
import com.maxinhai.platform.service.inventory.InventoryService;
import com.maxinhai.platform.service.order.ReceiptOrderService;
import com.maxinhai.platform.vo.order.ReceiptOrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReceiptOrderServiceImpl extends ServiceImpl<ReceiptOrderMapper, ReceiptOrder> implements ReceiptOrderService {

    @Resource
    private ReceiptOrderMapper receiptOrderMapper;
    @Resource
    private ReceiptOrderDetailMapper receiptOrderDetailMapper;
    @Resource
    private InventoryMapper inventoryMapper;
    @Resource
    private InventoryService inventoryService;
    @Resource
    private InventoryFlowService inventoryFlowService;
    @Resource
    private WarehouseLocationMapper locationMapper;
    @Resource
    private SystemFeignClient systemFeignClient;

    @Override
    public Page<ReceiptOrderVO> searchByPage(ReceiptOrderQueryDTO param) {
        Page<ReceiptOrderVO> pageResult = receiptOrderMapper.selectJoinPage(param.getPage(), ReceiptOrderVO.class,
                new MPJLambdaWrapper<ReceiptOrder>()
                        .like(StrUtil.isNotBlank(param.getOrderNo()), ReceiptOrder::getOrderNo, param.getOrderNo())
                        .eq(StrUtil.isNotBlank(param.getWarehouseId()), ReceiptOrder::getWarehouseId, param.getWarehouseId())
                        .orderByDesc(ReceiptOrder::getCreateTime));
        return pageResult;
    }

    @Override
    public ReceiptOrderVO getInfo(String id) {
        return receiptOrderMapper.selectJoinOne(ReceiptOrderVO.class, new MPJLambdaWrapper<ReceiptOrder>().eq(StrUtil.isNotBlank(id), ReceiptOrder::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        receiptOrderMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(ReceiptOrderEditDTO param) {
        ReceiptOrder order = BeanUtil.toBean(param, ReceiptOrder.class);
        receiptOrderMapper.updateById(order);
    }

    @Override
    public void add(ReceiptOrderAddDTO param) {
        List<String> codeList = systemFeignClient.generateCode("receipt", 1).getData();
        ReceiptOrder order = BeanUtil.toBean(param, ReceiptOrder.class);
        order.setOrderNo(codeList.get(0));
        receiptOrderMapper.insert(order);
    }

    @Override
    public void receipt(String id) {
        // 查询出库单信息
        ReceiptOrder transferOrder = receiptOrderMapper.selectOne(new LambdaQueryWrapper<ReceiptOrder>()
                .select(ReceiptOrder::getId, ReceiptOrder::getStatus, ReceiptOrder::getOrderNo)
                .eq(ReceiptOrder::getId, id));
        if (2 > transferOrder.getStatus()) {
            throw new BusinessException("出库单未审核通过！");
        }
        if (4 == transferOrder.getStatus()) {
            throw new BusinessException("出库单出库已完成！");
        }
        // 查询出库单明细
        List<ReceiptOrderDetail> orderDetailList = receiptOrderDetailMapper.selectList(new LambdaQueryWrapper<ReceiptOrderDetail>()
                .eq(ReceiptOrderDetail::getReceiptOrderId, id));
        // 查询库存明细
        List<String> locationIds = new ArrayList<>();
        List<String> materialIds = new ArrayList<>();
        for (ReceiptOrderDetail orderDetail : orderDetailList) {
            locationIds.add(orderDetail.getLocationId());
            materialIds.add(orderDetail.getMaterialId());
        }
        Map<String, Inventory> inventoryMap = inventoryMapper.selectList(new LambdaQueryWrapper<Inventory>()
                        .in(Inventory::getMaterialId, materialIds)
                        .in(Inventory::getLocationId, locationIds)).stream()
                .collect(Collectors.toMap(inventory -> inventory.getLocationId() + "_" + inventory.getMaterialId(),
                        inventory -> inventory,
                        (existing, replacement) -> existing));
        // 查询库位信息
        Map<String, WarehouseLocation> locationMap = locationMapper.selectList(new LambdaQueryWrapper<WarehouseLocation>()
                        .select(WarehouseLocation::getId, WarehouseLocation::getWarehouseId, WarehouseLocation::getAreaId, WarehouseLocation::getRackId)
                        .in(WarehouseLocation::getId, locationIds)).stream()
                .collect(Collectors.toMap(WarehouseLocation::getId, WarehouseLocation -> WarehouseLocation));
        // 出库
        List<Inventory> updateInventories = new ArrayList<>();
        for (ReceiptOrderDetail orderDetail : orderDetailList) {
            String targetLocationId = orderDetail.getLocationId();
            String materialId = orderDetail.getMaterialId();
            BigDecimal actualQty = orderDetail.getActualQty();

            Inventory targetInventory = inventoryMap.get(targetLocationId + "_" + materialId);
            if (Objects.isNull(targetInventory)) {
                targetInventory = new Inventory();
                WarehouseLocation location = locationMap.get(targetLocationId);
                targetInventory.setWarehouseId(location.getWarehouseId());
                targetInventory.setAreaId(location.getAreaId());
                targetInventory.setRackId(location.getRackId());
                targetInventory.setLocationId(orderDetail.getLocationId());
                targetInventory.setMaterialId(orderDetail.getMaterialId());
                targetInventory.setBatchNo(orderDetail.getBatchNo());
                targetInventory.setQty(orderDetail.getActualQty());
                targetInventory.setLockedQty(BigDecimal.ZERO);
                targetInventory.setAvailableQty(orderDetail.getActualQty());
                inventoryMapper.insert(targetInventory);
                //throw new BusinessException("目标货位库存明细不存在！");
            }
            // 目标库位库存-出库数量
            inventoryFlowService.createFlow(OperateType.RECEIPT, targetInventory, orderDetail.getActualQty(),
                    targetInventory.getQty(), targetInventory.getQty().subtract(actualQty));
            targetInventory.setQty(targetInventory.getQty().add(actualQty));

            updateInventories.add(targetInventory);
        }
        inventoryService.updateBatchById(updateInventories);
    }
}
