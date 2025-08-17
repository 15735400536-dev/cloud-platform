package com.maxinhai.platform.schedule;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.dto.OrderAddDTO;
import com.maxinhai.platform.po.Order;
import com.maxinhai.platform.po.TaskOrder;
import com.maxinhai.platform.po.WorkOrder;
import com.maxinhai.platform.enums.OrderStatus;
import com.maxinhai.platform.listener.TaskOrderReportEvent;
import com.maxinhai.platform.listener.TaskOrderResumeEvent;
import com.maxinhai.platform.listener.TaskOrderStartEvent;
import com.maxinhai.platform.mapper.OrderMapper;
import com.maxinhai.platform.mapper.TaskOrderMapper;
import com.maxinhai.platform.mapper.WorkOrderMapper;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class OrderSchedule {

    @Resource
    private TaskOrderMapper taskOrderMapper;
    @Resource
    private WorkOrderMapper workOrderMapper;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private ApplicationContext applicationContext;

    @Scheduled(initialDelay = 3000, fixedDelay = 120000)
    public void createOrderSchedule() {
        OrderAddDTO param = new OrderAddDTO();
        param.setOrderCode("SO_" + RandomUtil.randomString(6));
        param.setQty(10);
        param.setOrderType(1);
        param.setPlanBeginTime(new Date());
        param.setPlanEndTime(DateUtil.offsetDay(param.getPlanBeginTime(), 90));
        param.setProductId("1954171619052109826");
        param.setBomId("1954171628833226754");
        param.setRoutingId("1954171628384436226");
        AjaxResult ajaxResult = new RestTemplate().postForObject("http://localhost:10040/order/addOrder", param, AjaxResult.class);
        log.info("createOrder ajaxResult: {}", ajaxResult.toString());
    }

    /**
     * 开工
     */
    //@Scheduled(initialDelay = 3000, fixedDelay = 600000)
    public void startWork() {
        // 首道工序开工
        List<TaskOrder> taskOrderList = taskOrderMapper.selectJoinList(TaskOrder.class, new MPJLambdaWrapper<TaskOrder>()
                .innerJoin(WorkOrder.class, WorkOrder::getId, TaskOrder::getWorkOrderId)
                .innerJoin(Order.class, Order::getId, TaskOrder::getOrderId)
                // 字段别名
                .selectAll(TaskOrder.class)
                // 查询条件
                .eq(TaskOrder::getStatus, OrderStatus.INIT)
                .eq(WorkOrder::getOrderStatus, OrderStatus.INIT)
                .eq(Order::getOrderStatus, OrderStatus.INIT)
                .eq(TaskOrder::getSort, 1)
                .last("limit 500"));
        for (TaskOrder taskOrder : taskOrderList) {
            AjaxResult ajaxResult = new RestTemplate().getForObject("http://localhost:10040/taskOrder/startWork/" + taskOrder.getId(), AjaxResult.class);
            log.info("startWork ajaxResult: {}", ajaxResult.toString());
        }

        // 其他工序开工
        for (int sort = 2; sort <= 10; sort++) {
            taskOrderList = taskOrderMapper.selectJoinList(TaskOrder.class, new MPJLambdaWrapper<TaskOrder>()
                    .innerJoin(WorkOrder.class, WorkOrder::getId, TaskOrder::getWorkOrderId)
                    .innerJoin(Order.class, Order::getId, TaskOrder::getOrderId)
                    // 字段别名
                    .selectAll(TaskOrder.class)
                    // 查询条件
                    .eq(TaskOrder::getStatus, OrderStatus.INIT)
                    .eq(WorkOrder::getOrderStatus, OrderStatus.START)
                    .eq(Order::getOrderStatus, OrderStatus.START)
                    .eq(TaskOrder::getSort, sort)
                    .last("limit 500"));
            for (TaskOrder taskOrder : taskOrderList) {
                AjaxResult ajaxResult = new RestTemplate().getForObject("http://localhost:10040/taskOrder/startWork/" + taskOrder.getId(), AjaxResult.class);
                log.info("sort: {}, startWork ajaxResult: {}", sort, ajaxResult.toString());
            }
        }
    }

    /**
     * 暂停
     */
    //@Scheduled(initialDelay = 3000, fixedDelay = 120000)
    public void pauseWork() {
        List<TaskOrder> taskOrderList = taskOrderMapper.selectJoinList(TaskOrder.class, new MPJLambdaWrapper<TaskOrder>()
                .innerJoin(WorkOrder.class, WorkOrder::getId, TaskOrder::getWorkOrderId)
                .innerJoin(Order.class, Order::getId, TaskOrder::getOrderId)
                // 字段别名
                .selectAll(TaskOrder.class)
                // 查询条件
                .eq(TaskOrder::getStatus, OrderStatus.START)
                .last("limit 100"));
        for (TaskOrder taskOrder : taskOrderList) {
            AjaxResult ajaxResult = new RestTemplate().getForObject("http://localhost:10040/taskOrder/pauseWork/" + taskOrder.getId(), AjaxResult.class);
            log.info("pauseWork ajaxResult: {}", ajaxResult.toString());
        }
    }

    /**
     * 复工
     */
    //@Scheduled(initialDelay = 3000, fixedDelay = 240000)
    public void resumeWork() {
        List<TaskOrder> taskOrderList = taskOrderMapper.selectJoinList(TaskOrder.class, new MPJLambdaWrapper<TaskOrder>()
                .innerJoin(WorkOrder.class, WorkOrder::getId, TaskOrder::getWorkOrderId)
                .innerJoin(Order.class, Order::getId, TaskOrder::getOrderId)
                // 字段别名
                .selectAll(TaskOrder.class)
                // 查询条件
                .eq(TaskOrder::getStatus, OrderStatus.PAUSE)
                .last("limit 100"));
        for (TaskOrder taskOrder : taskOrderList) {
            AjaxResult ajaxResult = new RestTemplate().getForObject("http://localhost:10040/taskOrder/resumeWork/" + taskOrder.getId(), AjaxResult.class);
            log.info("resumeWork ajaxResult: {}", ajaxResult.toString());
        }
    }

    /**
     * 报工
     */
    //@Scheduled(initialDelay = 3000, fixedDelay = 360000)
    public void reportWork() {
        List<TaskOrder> taskOrderList = taskOrderMapper.selectJoinList(TaskOrder.class, new MPJLambdaWrapper<TaskOrder>()
                .innerJoin(WorkOrder.class, WorkOrder::getId, TaskOrder::getWorkOrderId)
                .innerJoin(Order.class, Order::getId, TaskOrder::getOrderId)
                // 字段别名
                .selectAll(TaskOrder.class)
                // 查询条件
                .eq(TaskOrder::getStatus, OrderStatus.START)
                .orderByAsc(TaskOrder::getCreateTime)
                .last("limit 1000"));
        for (TaskOrder taskOrder : taskOrderList) {
            AjaxResult ajaxResult = new RestTemplate().getForObject("http://localhost:10040/taskOrder/reportWork/" + taskOrder.getId(), AjaxResult.class);
            log.info("reportWork ajaxResult: {}", ajaxResult.toString());
        }
    }

    /**
     * 推送派工单
     */
    @Scheduled(initialDelay = 3000, fixedDelay = 5000)
    public void pushTaskOrder() {
        // 查询订单
        Order order = orderMapper.selectOne(new LambdaQueryWrapper<Order>()
                .select(Order::getId, Order::getOrderCode)
                .in(Order::getOrderStatus, OrderStatus.INIT, OrderStatus.START)
                .between(Order::getCreateTime, DateUtils.getBeginTimeOfToday(), DateUtils.getEndTimeOfToday())
                .orderByAsc(Order::getCreateTime)
                .last("limit 1"));
        if (Objects.isNull(order)) {
            return;
        }
        // 查询工单
        WorkOrder workOrder = workOrderMapper.selectOne(new LambdaQueryWrapper<WorkOrder>()
                .select(WorkOrder::getId, WorkOrder::getWorkOrderCode)
                .eq(WorkOrder::getOrderId, order.getId())
                .in(WorkOrder::getOrderStatus, OrderStatus.INIT, OrderStatus.START)
                .orderByAsc(WorkOrder::getCreateTime)
                .last("limit 1"));
        if (Objects.isNull(workOrder)) {
            return;
        }
        // 查询派工单
        TaskOrder taskOrder = taskOrderMapper.selectOne(new LambdaQueryWrapper<TaskOrder>()
                .select(TaskOrder::getId, TaskOrder::getTaskOrderCode, TaskOrder::getStatus)
                .eq(TaskOrder::getWorkOrderId, workOrder.getId())
                .ne(TaskOrder::getStatus, OrderStatus.REPORT)
                .orderByAsc(TaskOrder::getSort)
                .last("limit 1"));
        if (Objects.isNull(taskOrder)) {
            return;
        }
        // 派工单操作
        switch (taskOrder.getStatus()) {
            case INIT:
                applicationContext.publishEvent(new TaskOrderStartEvent(this, taskOrder.getId()));
                break;
            case PAUSE:
                applicationContext.publishEvent(new TaskOrderResumeEvent(this, taskOrder.getId()));
                break;
            case START:
                applicationContext.publishEvent(new TaskOrderReportEvent(this, taskOrder.getId()));
                break;
        }
    }

}
