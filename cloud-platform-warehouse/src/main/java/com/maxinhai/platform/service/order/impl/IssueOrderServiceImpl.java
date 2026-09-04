package com.maxinhai.platform.service.order.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.order.*;
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
import org.springframework.util.CollectionUtils;

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
        return issueOrderMapper.selectJoinPage(param.getPage(), IssueOrderVO.class,
                new MPJLambdaWrapper<IssueOrder>()
                        .like(StrUtil.isNotBlank(param.getOrderNo()), IssueOrder::getOrderNo, param.getOrderNo())
                        .like(StrUtil.isNotBlank(param.getWarehouseId()), IssueOrder::getWarehouseId, param.getWarehouseId())
                        .orderByDesc(IssueOrder::getCreateTime));
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

    @Override
    public void fullIssue(String orderId) {
        // 1. 查询明细
        List<IssueOrderDetail> detailList = issueOrderDetailMapper.selectList(new LambdaQueryWrapper<IssueOrderDetail>()
                .eq(IssueOrderDetail::getIssueOrderId, orderId));
        if (CollectionUtils.isEmpty(detailList)) {
            throw new RuntimeException("出库明细不存在");
        }

        List<IssueDetailDTO> issueDetailList = new ArrayList<>();
        for (IssueOrderDetail detail : detailList) {
            // 全部出库数量 = 计划数量 - 已出库数量
            BigDecimal outQty = detail.getPlanQty().subtract(detail.getActualQty());
            if (outQty.compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("该明细已全部出库，无需重复操作");
            }

            IssueDetailDTO issueDetail = new IssueDetailDTO();
            issueDetail.setIssueDetailId(detail.getId());
            issueDetail.setIssueQty(outQty);
            issueDetailList.add(issueDetail);
        }
        // 封装参数调用通用出库逻辑
        IssueDTO dto = new IssueDTO();
        dto.setIssueOrderId(detailList.get(0).getIssueOrderId());
        dto.setIssueDetailList(issueDetailList);
        partialOut(dto);
    }

    @Override
    public void partialOut(IssueDTO dto) {
        String detailId = ""; // dto.getDetailId();
        BigDecimal outQty = BigDecimal.ZERO; // dto.getOutQty();

        // 1. 参数校验
        if (outQty.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("出库数量必须大于0");
        }

        // 2. 查询出库明细、主单
        IssueOrderDetail detail = issueOrderDetailMapper.selectById(detailId);
        if (detail == null) {
            throw new RuntimeException("出库明细不存在");
        }
        IssueOrder mainOrder = issueOrderMapper.selectById(detail.getIssueOrderId());
        if (mainOrder == null) {
            throw new RuntimeException("出库主单不存在");
        }

        // 3. 单据状态校验：仅【已审核】单据允许出库
        if (!Integer.valueOf(2).equals(mainOrder.getStatus())) {
            throw new RuntimeException("仅已审核状态的出库单可执行出库操作");
        }

        // 4. 校验剩余可出库数量
        BigDecimal remainQty = detail.getPlanQty().subtract(detail.getActualQty());
        if (outQty.compareTo(remainQty) > 0) {
            throw new RuntimeException("本次出库数量超出剩余可出库数量，剩余：" + remainQty);
        }

        // 5. 查询对应库存（仓库+货位+物料+批次唯一库存）
        Inventory inventory = inventoryMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Inventory>()
                        .eq(Inventory::getWarehouseId, mainOrder.getWarehouseId())
                        .eq(Inventory::getLocationId, detail.getLocationId())
                        .eq(Inventory::getMaterialId, detail.getMaterialId())
                        .eq(Inventory::getBatchNo, detail.getBatchNo())
        );
        if (inventory == null) {
            throw new RuntimeException("未查询到对应批次库存，无法出库");
        }

        // 6. 校验可用库存充足
        if (inventory.getAvailableQty().compareTo(outQty) < 0) {
            throw new RuntimeException("可用库存不足，当前可用：" + inventory.getAvailableQty());
        }

        // 7. 更新明细实际出库数量
        BigDecimal newActual = detail.getActualQty().add(outQty);
        LambdaUpdateWrapper<IssueOrderDetail> detailUpd = new LambdaUpdateWrapper<>();
        detailUpd.eq(IssueOrderDetail::getId, detailId)
                .set(IssueOrderDetail::getActualQty, newActual);
        issueOrderDetailMapper.update(null, detailUpd);

        // 8. 扣减库存总数量、可用数量
        LambdaUpdateWrapper<Inventory> invUpd = new LambdaUpdateWrapper<>();
        invUpd.eq(Inventory::getId, inventory.getId())
                .setSql("qty = qty - " + outQty)
                .setSql("available_qty = available_qty - " + outQty);
        inventoryMapper.update(null, invUpd);

        // 9. 判断当前主单所有明细是否全部出库完成，更新主单状态为已完成(3)
        checkAndUpdateMainOrderStatus(detail.getIssueOrderId());
    }

    /**
     * 校验出库单所有明细是否全部出库，更新主单状态
     */
    private void checkAndUpdateMainOrderStatus(String orderId) {
        // 查询该单下所有明细
        List<IssueOrderDetail> detailList = issueOrderDetailMapper.selectList(
                new LambdaQueryWrapper<IssueOrderDetail>()
                        .eq(IssueOrderDetail::getIssueOrderId, orderId)
        );
        // 判断是否存在未出库完毕的明细
        boolean allFinish = detailList.stream().allMatch(d ->
                d.getActualQty().compareTo(d.getPlanQty()) >= 0
        );
        if (allFinish) {
            LambdaUpdateWrapper<IssueOrder> issueOrder = new LambdaUpdateWrapper<>();
            issueOrder.eq(IssueOrder::getId, orderId)
                      .set(IssueOrder::getStatus, 3); // 3=已完成
            issueOrderMapper.update(null, issueOrder);
        }
    }

}
