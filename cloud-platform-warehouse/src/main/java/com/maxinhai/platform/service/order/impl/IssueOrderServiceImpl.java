package com.maxinhai.platform.service.order.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.order.IssueOrderAddDTO;
import com.maxinhai.platform.dto.order.IssueOrderEditDTO;
import com.maxinhai.platform.dto.order.IssueOrderQueryDTO;
import com.maxinhai.platform.enums.OperateType;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.feign.SystemFeignClient;
import com.maxinhai.platform.mapper.inventory.InventoryMapper;
import com.maxinhai.platform.mapper.model.WarehouseLocationMapper;
import com.maxinhai.platform.mapper.order.IssueOrderDetailMapper;
import com.maxinhai.platform.mapper.order.IssueOrderMapper;
import com.maxinhai.platform.po.inventory.Inventory;
import com.maxinhai.platform.po.model.WarehouseLocation;
import com.maxinhai.platform.po.order.IssueOrder;
import com.maxinhai.platform.po.order.IssueOrderDetail;
import com.maxinhai.platform.service.inventory.InventoryFlowService;
import com.maxinhai.platform.service.inventory.InventoryService;
import com.maxinhai.platform.service.order.IssueOrderService;
import com.maxinhai.platform.vo.order.IssueOrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class IssueOrderServiceImpl extends ServiceImpl<IssueOrderMapper, IssueOrder> implements IssueOrderService {

    @Resource
    private IssueOrderMapper issueOrderMapper;
    @Resource
    private IssueOrderDetailMapper issueOrderDetailMapper;
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
    public Page<IssueOrderVO> searchByPage(IssueOrderQueryDTO param) {
        Page<IssueOrderVO> pageResult = issueOrderMapper.selectJoinPage(param.getPage(), IssueOrderVO.class,
                new MPJLambdaWrapper<IssueOrder>()
                        .like(StrUtil.isNotBlank(param.getOrderNo()), IssueOrder::getOrderNo, param.getOrderNo())
                        .like(StrUtil.isNotBlank(param.getWarehouseId()), IssueOrder::getWarehouseId, param.getWarehouseId())
                        .orderByDesc(IssueOrder::getCreateTime));
        return pageResult;
    }

    @Override
    public IssueOrderVO getInfo(String id) {
        return issueOrderMapper.selectJoinOne(IssueOrderVO.class, new MPJLambdaWrapper<IssueOrder>().eq(StrUtil.isNotBlank(id), IssueOrder::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        issueOrderMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
        issueOrderDetailMapper.delete(new LambdaQueryWrapper<IssueOrderDetail>()
                .in(IssueOrderDetail::getIssueOrderId, ids));
    }

    @Override
    public void edit(IssueOrderEditDTO param) {
        IssueOrder order = BeanUtil.toBean(param, IssueOrder.class);
        issueOrderMapper.updateById(order);
    }

    @Override
    public void add(IssueOrderAddDTO param) {
        List<String> codeList = systemFeignClient.generateCode("issue", 1).getData();
        IssueOrder order = BeanUtil.toBean(param, IssueOrder.class);
        order.setOrderNo(codeList.get(0));
        issueOrderMapper.insert(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void issue(String id) {
        // 查询入库单信息
        IssueOrder transferOrder = issueOrderMapper.selectOne(new LambdaQueryWrapper<IssueOrder>()
                .select(IssueOrder::getId, IssueOrder::getStatus, IssueOrder::getOrderNo)
                .eq(IssueOrder::getId, id));
        if (2 > transferOrder.getStatus()) {
            throw new BusinessException("入库单未审核通过！");
        }
        if (4 == transferOrder.getStatus()) {
            throw new BusinessException("入库单入库已完成！");
        }
        // 查询入库单明细
        List<IssueOrderDetail> orderDetailList = issueOrderDetailMapper.selectList(new LambdaQueryWrapper<IssueOrderDetail>()
                .eq(IssueOrderDetail::getIssueOrderId, id));
        // 查询库存明细
        List<String> locationIds = new ArrayList<>();
        List<String> materialIds = new ArrayList<>();
        for (IssueOrderDetail orderDetail : orderDetailList) {
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
        // 入库
        List<Inventory> updateInventories = new ArrayList<>();
        for (IssueOrderDetail orderDetail : orderDetailList) {
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
            // 目标库位库存+入库数量
            inventoryFlowService.createFlow(OperateType.ISSUE, targetInventory, orderDetail.getActualQty(),
                    targetInventory.getQty(), targetInventory.getQty().add(actualQty));
            targetInventory.setQty(targetInventory.getQty().add(actualQty));

            updateInventories.add(targetInventory);
        }
        inventoryService.updateBatchById(updateInventories);
    }
}
