package com.maxinhai.platform.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.TaskOrderQueryDTO;
import com.maxinhai.platform.enums.OperateType;
import com.maxinhai.platform.enums.OrderStatus;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.listener.CheckOrderEvent;
import com.maxinhai.platform.listener.OperationCheckOrderEvent;
import com.maxinhai.platform.mapper.OrderMapper;
import com.maxinhai.platform.mapper.TaskOrderMapper;
import com.maxinhai.platform.mapper.WorkOrderMapper;
import com.maxinhai.platform.po.*;
import com.maxinhai.platform.po.technology.Bom;
import com.maxinhai.platform.po.technology.Operation;
import com.maxinhai.platform.po.technology.Routing;
import com.maxinhai.platform.service.OperateRecordService;
import com.maxinhai.platform.service.TaskOrderService;
import com.maxinhai.platform.utils.DateUtils;
import com.maxinhai.platform.vo.TaskOrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TaskOrderServiceImpl extends ServiceImpl<TaskOrderMapper, TaskOrder> implements TaskOrderService {

    @Resource
    private TaskOrderMapper taskOrderMapper;
    @Resource
    private WorkOrderMapper workOrderMapper;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private OperateRecordService operateRecordService;
    @Resource
    private ApplicationContext applicationContext;

    @Override
    public Page<TaskOrderVO> searchByPage(TaskOrderQueryDTO param) {
        return taskOrderMapper.selectJoinPage(param.getPage(), TaskOrderVO.class,
                new MPJLambdaWrapper<TaskOrder>()
                        .innerJoin(Product.class, Product::getId, Order::getProductId)
                        .innerJoin(Bom.class, Bom::getId, Order::getBomId)
                        .innerJoin(Routing.class, Routing::getId, Order::getRoutingId)
                        .innerJoin(Operation.class, Operation::getId, TaskOrder::getOperationId)
                        // 查询条件
                        .like(StrUtil.isNotBlank(param.getTaskOrderCode()), TaskOrder::getTaskOrderCode, param.getTaskOrderCode())
                        .eq(Objects.nonNull(param.getStatus()), TaskOrder::getStatus, param.getStatus())
                        // 字段映射
                        .selectAll(TaskOrder.class)
                        .selectAs(Product::getCode, TaskOrderVO::getProductCode)
                        .selectAs(Product::getName, TaskOrderVO::getProductName)
                        .selectAs(Bom::getCode, TaskOrderVO::getBomCode)
                        .selectAs(Bom::getName, TaskOrderVO::getBomName)
                        .selectAs(Routing::getCode, TaskOrderVO::getRoutingCode)
                        .selectAs(Routing::getName, TaskOrderVO::getRoutingName)
                        .selectAs(Operation::getCode, TaskOrderVO::getOperationCode)
                        .selectAs(Operation::getName, TaskOrderVO::getOperationName)
                        // 排序
                        .orderByDesc(TaskOrder::getCreateTime));
    }

    @Override
    public TaskOrderVO getInfo(String id) {
        return taskOrderMapper.selectJoinOne(TaskOrderVO.class, new MPJLambdaWrapper<TaskOrder>()
                .innerJoin(Product.class, Product::getId, Order::getProductId)
                .innerJoin(Bom.class, Bom::getId, Order::getBomId)
                .innerJoin(Routing.class, Routing::getId, Order::getRoutingId)
                .innerJoin(Operation.class, Operation::getId, TaskOrder::getOperationId)
                // 字段映射
                .selectAll(TaskOrder.class)
                .selectAs(Product::getCode, TaskOrderVO::getProductCode)
                .selectAs(Product::getName, TaskOrderVO::getProductName)
                .selectAs(Bom::getCode, TaskOrderVO::getBomCode)
                .selectAs(Bom::getName, TaskOrderVO::getBomName)
                .selectAs(Routing::getCode, TaskOrderVO::getRoutingCode)
                .selectAs(Routing::getName, TaskOrderVO::getRoutingName)
                // 查询条件
                .eq(TaskOrder::getId, id));
    }

    @Override
    public void remove(String[] ids) {
        taskOrderMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void startWork(String taskOrderId) {
        TaskOrder taskOrder = taskOrderMapper.selectById(taskOrderId);
        if (Objects.isNull(taskOrder)) {
            throw new BusinessException("派工单不存在！");
        }

        TaskOrder preTaskOrder = getPreTaskOrder(taskOrder.getWorkOrderId(), taskOrderId);
        if (Objects.nonNull(preTaskOrder) && !OrderStatus.REPORT.equals(preTaskOrder.getStatus())) {
            log.error("派工单【{}】开工失败，上道工序未报工！", taskOrder.getId());
            throw new BusinessException("派工单开工失败，上道工序未报工！");
        }

        if (!OrderStatus.INIT.equals(taskOrder.getStatus())) {
            StringBuilder buffer = new StringBuilder("派工单开工失败，");
            switch (taskOrder.getStatus()) {
                case START:
                    buffer.append("派工单已开工!");
                    break;
                case PAUSE:
                    buffer.append("派工单已暂停!");
                    break;
                case RESUME:
                    buffer.append("派工单已复工!");
                    break;
                case REPORT:
                    buffer.append("派工单已报工!");
                    break;
                default:
                    buffer.append("派工单未知状态!");
                    break;
            }
            throw new BusinessException(buffer.toString());
        }
        // 更新派工单状态
        taskOrder.setStatus(OrderStatus.START);
        taskOrder.setActualBeginTime(new Date());
        taskOrderMapper.updateById(taskOrder);

        // 更新工单状态
        if (!checkWorkOrderStart(taskOrder.getWorkOrderId())) {
            WorkOrder workOrder = workOrderMapper.selectById(taskOrder.getWorkOrderId());
            workOrder.setOrderStatus(OrderStatus.START);
            workOrder.setActualBeginTime(taskOrder.getActualBeginTime());
            workOrderMapper.updateById(workOrder);
        }

        // 更新订单状态
        if (!checkOrderStart(taskOrder.getOrderId())) {
            Order order = orderMapper.selectById(taskOrder.getOrderId());
            order.setOrderStatus(OrderStatus.START);
            order.setActualBeginTime(taskOrder.getActualBeginTime());
            orderMapper.updateById(order);
        }

        // 创建开工记录
        operateRecordService.createRecord(OperateType.START, taskOrder.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pauseWork(String taskId) {
        TaskOrder taskOrder = taskOrderMapper.selectById(taskId);
        if (Objects.isNull(taskOrder)) {
            throw new BusinessException("派工单不存在！");
        }
        if (!OrderStatus.START.equals(taskOrder.getStatus())) {
            StringBuilder buffer = new StringBuilder("派工单暂停失败，");
            switch (taskOrder.getStatus()) {
                case INIT:
                    buffer.append("派工单未开工!");
                    break;
                case PAUSE:
                    buffer.append("派工单已暂停!");
                    break;
                case RESUME:
                    buffer.append("派工单已复工!");
                    break;
                case REPORT:
                    buffer.append("派工单已报工!");
                    break;
                default:
                    buffer.append("派工单未知状态!");
                    break;
            }
            throw new BusinessException(buffer.toString());
        }
        // 更新派工单状态
        taskOrder.setStatus(OrderStatus.PAUSE);
        taskOrderMapper.updateById(taskOrder);

        // 更新工单状态
        WorkOrder workOrder = workOrderMapper.selectById(taskOrder.getWorkOrderId());
        workOrder.setOrderStatus(OrderStatus.PAUSE);
        workOrderMapper.updateById(workOrder);

        // 更新订单状态
        Order order = orderMapper.selectById(taskOrder.getOrderId());
        order.setOrderStatus(OrderStatus.PAUSE);
        orderMapper.updateById(order);

        // 创建暂停记录
        operateRecordService.createRecord(OperateType.PAUSE, taskOrder.getId());
    }

    @Override
    public void resumeWork(String taskId) {
        TaskOrder taskOrder = taskOrderMapper.selectById(taskId);
        if (Objects.isNull(taskOrder)) {
            throw new BusinessException("派工单不存在！");
        }
        if (!OrderStatus.PAUSE.equals(taskOrder.getStatus())) {
            StringBuilder buffer = new StringBuilder("派工单复工失败，");
            switch (taskOrder.getStatus()) {
                case INIT:
                    buffer.append("派工单未开工!");
                    break;
                case START:
                    buffer.append("派工单已开工!");
                    break;
                case RESUME:
                    buffer.append("派工单已复工!");
                    break;
                case REPORT:
                    buffer.append("派工单已报工!");
                    break;
                default:
                    buffer.append("派工单未知状态!");
                    break;
            }
            throw new BusinessException(buffer.toString());
        }
        // 更新派工单状态
        taskOrder.setStatus(OrderStatus.START);
        taskOrderMapper.updateById(taskOrder);

        // 更新工单状态
        WorkOrder workOrder = workOrderMapper.selectById(taskOrder.getWorkOrderId());
        workOrder.setOrderStatus(OrderStatus.START);
        workOrderMapper.updateById(workOrder);

        // 更新订单状态
        Order order = orderMapper.selectById(taskOrder.getOrderId());
        order.setOrderStatus(OrderStatus.START);
        orderMapper.updateById(order);

        // 创建复工记录
        operateRecordService.createRecord(OperateType.RESUME, taskOrder.getId());
    }

    @Override
    public void reportWork(String taskId) {
        TaskOrder taskOrder = taskOrderMapper.selectById(taskId);
        if (Objects.isNull(taskOrder)) {
            throw new BusinessException("派工单不存在！");
        }
        if (!OrderStatus.START.equals(taskOrder.getStatus())) {
            StringBuilder buffer = new StringBuilder("派工单复工失败，");
            switch (taskOrder.getStatus()) {
                case INIT:
                    buffer.append("派工单未开工!");
                    break;
                case PAUSE:
                    buffer.append("派工单已暂停!");
                    break;
                case RESUME:
                    buffer.append("派工单已复工!");
                    break;
                case REPORT:
                    buffer.append("派工单已报工!");
                    break;
                default:
                    buffer.append("派工单未知状态!");
                    break;
            }
            throw new BusinessException(buffer.toString());
        }
        // 更新派工单状态
        taskOrder.setStatus(OrderStatus.REPORT);
        taskOrder.setActualEndTime(new Date());
        taskOrderMapper.updateById(taskOrder);

        // 更新工单状态
        if (checkWorkOrderReport(taskOrder.getWorkOrderId())) {
            WorkOrder workOrder = workOrderMapper.selectById(taskOrder.getWorkOrderId());
            workOrder.setOrderStatus(OrderStatus.REPORT);
            workOrder.setActualEndTime(taskOrder.getActualEndTime());
            workOrderMapper.updateById(workOrder);
            // 工单完工，生成质检单
            applicationContext.publishEvent(new CheckOrderEvent(this, workOrder.getId()));
        }

        // 更新订单状态
        if (checkOrderReport(taskOrder.getOrderId())) {
            Order order = orderMapper.selectById(taskOrder.getOrderId());
            order.setOrderStatus(OrderStatus.REPORT);
            order.setActualEndTime(taskOrder.getActualEndTime());
            orderMapper.updateById(order);
        }

        // 创建报工记录
        operateRecordService.createRecord(OperateType.REPORT, taskOrder.getId());

        // 派工单完工，生成工序质检单
        applicationContext.publishEvent(new OperationCheckOrderEvent(this, taskOrder.getId()));
    }

    @Override
    public TaskOrder getPreTaskOrder(String workOrderId, String taskOrderId) {
        List<TaskOrder> taskOrderList = taskOrderMapper.selectList(new LambdaQueryWrapper<TaskOrder>()
                .select(TaskOrder::getId, TaskOrder::getStatus, TaskOrder::getSort)
                .eq(TaskOrder::getWorkOrderId, workOrderId)
                .orderByAsc(TaskOrder::getSort));
        int currentIndex = -1;
        for (int index = 0; index < taskOrderList.size(); index++) {
            TaskOrder taskOrder = taskOrderList.get(index);
            if (taskOrder.getId().equals(taskOrderId)) {
                currentIndex = index;
                break;
            }
        }
        if (currentIndex == -1) {
            throw new BusinessException("未找到当前派工单！");
        }

        int nextIndex = currentIndex - 1;
        if (nextIndex < 0) {
            // 当前派工单是第一道工序或者最后一道工序，返回当前派工单，或者返回空
            return null;
        }
        return taskOrderList.get(nextIndex);
    }

    @Override
    public TaskOrder getNextTaskOrder(String workOrderId, String taskOrderId) {
        List<TaskOrder> taskOrderList = taskOrderMapper.selectList(new LambdaQueryWrapper<TaskOrder>()
                .select(TaskOrder::getId, TaskOrder::getStatus, TaskOrder::getSort)
                .eq(TaskOrder::getWorkOrderId, workOrderId)
                .orderByAsc(TaskOrder::getSort));
        int currentIndex = -1;
        for (int index = 0; index < taskOrderList.size(); index++) {
            TaskOrder taskOrder = taskOrderList.get(index);
            if (taskOrder.getId().equals(taskOrderId)) {
                currentIndex = index;
                break;
            }
        }
        if (currentIndex == -1) {
            throw new BusinessException("未找到当前派工单！");
        }

        int nextIndex = currentIndex + 1;
        if (nextIndex >= taskOrderList.size()) {
            // 当前派工单是第一道工序或者最后一道工序，返回当前派工单，或者返回空
            return null;
        }
        return taskOrderList.get(nextIndex);
    }

    @Override
    public boolean checkOrderStart(String orderId) {
        List<WorkOrder> workOrderList = workOrderMapper.selectList(new LambdaQueryWrapper<WorkOrder>()
                .select(WorkOrder::getId, WorkOrder::getOrderId, WorkOrder::getOrderStatus)
                .eq(WorkOrder::getOrderStatus, OrderStatus.START)
                .eq(WorkOrder::getOrderId, orderId));
        return !workOrderList.isEmpty();
    }

    @Override
    public boolean checkOrderReport(String orderId) {
        List<WorkOrder> workOrderList = workOrderMapper.selectList(new LambdaQueryWrapper<WorkOrder>()
                .select(WorkOrder::getId, WorkOrder::getOrderId, WorkOrder::getOrderStatus)
                .eq(WorkOrder::getOrderId, orderId));
        long reportCount = workOrderList.stream()
                .filter(workOrder -> workOrder.getOrderStatus().equals(OrderStatus.REPORT))
                .count();
        return workOrderList.size() == reportCount;
    }

    @Override
    public boolean checkWorkOrderStart(String workOrderId) {
        List<TaskOrder> taskOrderList = taskOrderMapper.selectList(new LambdaQueryWrapper<TaskOrder>()
                .select(TaskOrder::getId, TaskOrder::getWorkOrderId, TaskOrder::getStatus)
                .eq(TaskOrder::getStatus, OrderStatus.START)
                .eq(TaskOrder::getWorkOrderId, workOrderId));
        return !taskOrderList.isEmpty();
    }

    @Override
    public boolean checkWorkOrderReport(String workOrderId) {
        List<TaskOrder> taskOrderList = taskOrderMapper.selectList(new LambdaQueryWrapper<TaskOrder>()
                .select(TaskOrder::getId, TaskOrder::getOrderId, TaskOrder::getStatus)
                .eq(TaskOrder::getWorkOrderId, workOrderId));
        long reportCount = taskOrderList.stream()
                .filter(taskOrder -> taskOrder.getStatus().equals(OrderStatus.REPORT))
                .count();
        return taskOrderList.size() == reportCount;
    }

    @Override
    public long getTodayFinishTaskOrderCount() {
        return taskOrderMapper.selectJoinCount(new MPJLambdaWrapper<TaskOrder>()
                .innerJoin(WorkOrder.class, WorkOrder::getId, TaskOrder::getWorkOrderId)
                .innerJoin(Order.class, Order::getId, TaskOrder::getOrderId)
                .eq(TaskOrder::getStatus, OrderStatus.REPORT)
                .between(TaskOrder::getActualEndTime, DateUtils.getBeginTimeOfToday(), DateUtils.getEndTimeOfToday()));
    }
}
