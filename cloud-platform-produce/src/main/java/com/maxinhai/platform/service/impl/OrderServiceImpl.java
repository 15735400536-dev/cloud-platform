package com.maxinhai.platform.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.OrderAddDTO;
import com.maxinhai.platform.dto.OrderQueryDTO;
import com.maxinhai.platform.po.*;
import com.maxinhai.platform.enums.OrderStatus;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.mapper.OrderMapper;
import com.maxinhai.platform.mapper.RoutingOperationRelMapper;
import com.maxinhai.platform.mapper.TaskOrderMapper;
import com.maxinhai.platform.mapper.WorkOrderMapper;
import com.maxinhai.platform.po.technology.Bom;
import com.maxinhai.platform.po.technology.Routing;
import com.maxinhai.platform.po.technology.RoutingOperationRel;
import com.maxinhai.platform.service.OrderService;
import com.maxinhai.platform.utils.DateUtils;
import com.maxinhai.platform.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private WorkOrderMapper workOrderMapper;
    @Resource
    private TaskOrderMapper taskOrderMapper;
    @Resource
    private RoutingOperationRelMapper routingOperationRelMapper;

    @Override
    public Page<OrderVO> searchByPage(OrderQueryDTO param) {
        return orderMapper.selectJoinPage(param.getPage(), OrderVO.class,
                new MPJLambdaWrapper<Order>()
                        .innerJoin(Product.class, Product::getId, Order::getProductId)
                        .innerJoin(Bom.class, Bom::getId, Order::getBomId)
                        .innerJoin(Routing.class, Routing::getId, Order::getRoutingId)
                        // 查询条件
                        .eq(StrUtil.isNotBlank(param.getOrderCode()), Order::getOrderCode, param.getOrderCode())
                        .eq(Objects.nonNull(param.getOrderType()), Order::getOrderType, param.getOrderType())
                        .eq(Objects.nonNull(param.getOrderStatus()), Order::getOrderStatus, param.getOrderStatus())
                        // 字段映射
                        .selectAll(Order.class)
                        .selectAs(Product::getCode, OrderVO::getProductCode)
                        .selectAs(Product::getName, OrderVO::getProductName)
                        .selectAs(Bom::getCode, OrderVO::getBomCode)
                        .selectAs(Bom::getName, OrderVO::getBomName)
                        .selectAs(Routing::getCode, OrderVO::getRoutingCode)
                        .selectAs(Routing::getName, OrderVO::getRoutingName)
                        // 排序
                        .orderByDesc(Order::getCreateTime));
    }

    @Override
    public OrderVO getInfo(String id) {
        return orderMapper.selectJoinOne(OrderVO.class, new MPJLambdaWrapper<Order>()
                .innerJoin(Product.class, Product::getId, Order::getProductId)
                .innerJoin(Bom.class, Bom::getId, Order::getBomId)
                .innerJoin(Routing.class, Routing::getId, Order::getRoutingId)
                // 字段映射
                .selectAll(Order.class)
                .selectAs(Product::getCode, OrderVO::getProductCode)
                .selectAs(Product::getName, OrderVO::getProductName)
                .selectAs(Bom::getCode, OrderVO::getBomCode)
                .selectAs(Bom::getName, OrderVO::getBomName)
                .selectAs(Routing::getCode, OrderVO::getRoutingCode)
                .selectAs(Routing::getName, OrderVO::getRoutingName)
                // 查询条件
                .eq(Order::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        orderMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void add(OrderAddDTO param) {
        // 查询工艺路线明细
        List<RoutingOperationRel> relList = routingOperationRelMapper.selectList(new LambdaQueryWrapper<RoutingOperationRel>()
                .select(RoutingOperationRel::getRoutingId, RoutingOperationRel::getOperationId, RoutingOperationRel::getSort)
                .eq(RoutingOperationRel::getRoutingId, param.getRoutingId()));
        if(CollectionUtils.isEmpty(relList)) {
            throw new BusinessException("工艺路线不存在!");
        }

        // 创建订单
        Order order = new Order();
        order.setOrderCode(param.getOrderCode());
        order.setQty(param.getQty());
        order.setOrderType(param.getOrderType());
        order.setOrderTime(new Date());
        order.setOrderStatus(OrderStatus.INIT);
        order.setPlanBeginTime(param.getPlanBeginTime());
        order.setPlanEndTime(param.getPlanEndTime());
        order.setProductId(param.getProductId());
        order.setBomId(param.getBomId());
        order.setRoutingId(param.getRoutingId());
        orderMapper.insert(order);
        // 创建工单
        for (Integer i = 0; i < order.getQty(); i++) {
            WorkOrder workOrder = getWorkOrder(param, order, i);
            workOrderMapper.insert(workOrder);

            // 创建派工单
            for (RoutingOperationRel rel : relList) {
                TaskOrder taskOrder = new TaskOrder();
                taskOrder.setTaskOrderCode(workOrder.getWorkOrderCode() + "_" + rel.getSort());
                taskOrder.setStatus(OrderStatus.INIT);
                taskOrder.setPlanBeginTime(param.getPlanBeginTime());
                taskOrder.setPlanEndTime(param.getPlanEndTime());
                taskOrder.setProductId(workOrder.getProductId());
                taskOrder.setBomId(workOrder.getBomId());
                taskOrder.setRoutingId(workOrder.getRoutingId());
                taskOrder.setOperationId(rel.getOperationId());
                taskOrder.setSort(rel.getSort());
                taskOrder.setOrderId(order.getId());
                taskOrder.setWorkOrderId(workOrder.getId());
                taskOrderMapper.insert(taskOrder);
            }
        }
    }

    @NotNull
    private static WorkOrder getWorkOrder(OrderAddDTO param, Order order, Integer i) {
        WorkOrder workOrder = new WorkOrder();
        workOrder.setWorkOrderCode(order.getOrderCode() + "_" + (i + 1));
        workOrder.setQty(1);
        workOrder.setOrderStatus(OrderStatus.INIT);
        workOrder.setPlanBeginTime(param.getPlanBeginTime());
        workOrder.setPlanEndTime(param.getPlanEndTime());
        workOrder.setProductId(order.getProductId());
        workOrder.setBomId(order.getBomId());
        workOrder.setRoutingId(order.getRoutingId());
        workOrder.setOrderId(order.getId());
        return workOrder;
    }

    @Override
    public long getTodayFinishOrderCount() {
        return orderMapper.selectJoinCount(new MPJLambdaWrapper<Order>()
                .eq(Order::getOrderStatus, OrderStatus.REPORT)
                .between(Order::getActualEndTime, DateUtils.getBeginTimeOfToday(), DateUtils.getEndTimeOfToday()));
    }
}
