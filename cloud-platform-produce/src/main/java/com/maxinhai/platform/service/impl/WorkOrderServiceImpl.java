package com.maxinhai.platform.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.WorkOrderQueryDTO;
import com.maxinhai.platform.po.*;
import com.maxinhai.platform.enums.OrderStatus;
import com.maxinhai.platform.mapper.WorkOrderMapper;
import com.maxinhai.platform.po.technology.Bom;
import com.maxinhai.platform.po.technology.Routing;
import com.maxinhai.platform.service.WorkOrderService;
import com.maxinhai.platform.utils.DateUtils;
import com.maxinhai.platform.vo.WorkOrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class WorkOrderServiceImpl extends ServiceImpl<WorkOrderMapper, WorkOrder> implements WorkOrderService {

    @Resource
    private WorkOrderMapper workOrderMapper;

    @Override
    public Page<WorkOrderVO> searchByPage(WorkOrderQueryDTO param) {
        return workOrderMapper.selectJoinPage(param.getPage(), WorkOrderVO.class,
                new MPJLambdaWrapper<WorkOrder>()
                        .innerJoin(Product.class, Product::getId, Order::getProductId)
                        .innerJoin(Bom.class, Bom::getId, Order::getBomId)
                        .innerJoin(Routing.class, Routing::getId, Order::getRoutingId)
                        // 查询条件
                        .like(StrUtil.isNotBlank(param.getWorkOrderCode()), WorkOrder::getWorkOrderCode, param.getWorkOrderCode())
                        .eq(Objects.nonNull(param.getOrderStatus()), WorkOrder::getOrderStatus, param.getOrderStatus())
                        // 字段映射
                        .selectAll(WorkOrder.class)
                        .selectAs(Product::getCode, WorkOrderVO::getProductCode)
                        .selectAs(Product::getName, WorkOrderVO::getProductName)
                        .selectAs(Bom::getCode, WorkOrderVO::getBomCode)
                        .selectAs(Bom::getName, WorkOrderVO::getBomName)
                        .selectAs(Routing::getCode, WorkOrderVO::getRoutingCode)
                        .selectAs(Routing::getName, WorkOrderVO::getRoutingName)
                        // 排序
                        .orderByDesc(WorkOrder::getCreateTime));
    }

    @Override
    public WorkOrderVO getInfo(String id) {
        return workOrderMapper.selectJoinOne(WorkOrderVO.class, new MPJLambdaWrapper<WorkOrder>()
                .innerJoin(Product.class, Product::getId, Order::getProductId)
                .innerJoin(Bom.class, Bom::getId, Order::getBomId)
                .innerJoin(Routing.class, Routing::getId, Order::getRoutingId)
                // 字段映射
                .selectAll(WorkOrder.class)
                .selectAs(Product::getCode, WorkOrderVO::getProductCode)
                .selectAs(Product::getName, WorkOrderVO::getProductName)
                .selectAs(Bom::getCode, WorkOrderVO::getBomCode)
                .selectAs(Bom::getName, WorkOrderVO::getBomName)
                .selectAs(Routing::getCode, WorkOrderVO::getRoutingCode)
                .selectAs(Routing::getName, WorkOrderVO::getRoutingName)
                // 查询条件
                .eq(WorkOrder::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        workOrderMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public long getTodayFinishWorkOrderCount() {
        return workOrderMapper.selectJoinCount(new MPJLambdaWrapper<WorkOrder>()
                .innerJoin(Order.class, Order::getId, WorkOrder::getOrderId)
                .eq(WorkOrder::getOrderStatus, OrderStatus.REPORT)
                .between(WorkOrder::getActualEndTime, DateUtils.getBeginTimeOfToday(), DateUtils.getEndTimeOfToday()));
    }
}
