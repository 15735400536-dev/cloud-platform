package com.maxinhai.platform.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.google.common.collect.Lists;
import com.maxinhai.platform.bo.DailyProcessFinishTaskOrderQtyBO;
import com.maxinhai.platform.bo.DailyTaskOrderBO;
import com.maxinhai.platform.dto.TaskOrderQueryDTO;
import com.maxinhai.platform.enums.OperateType;
import com.maxinhai.platform.enums.OrderStatus;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.listener.CheckOrderEvent;
import com.maxinhai.platform.listener.OperationCheckOrderEvent;
import com.maxinhai.platform.mapper.OperateRecordMapper;
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
import com.maxinhai.platform.vo.DailyOpTaskOrderVO;
import com.maxinhai.platform.vo.DailyTaskOrderVO;
import com.maxinhai.platform.vo.TaskOrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TaskOrderServiceImpl extends ServiceImpl<TaskOrderMapper, TaskOrder> implements TaskOrderService {

    // 全局锁：防止同一个taskOrderId并发穿透数据库
    private final ConcurrentHashMap<String, ReentrantLock> loadLockMap = new ConcurrentHashMap<>();
    // 订单ID -> (工单ID -> (派工单ID -> 派工单))
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, ConcurrentHashMap<String, TaskOrder>>> taskOrderCache = new ConcurrentHashMap<>();
    // 订单ID -> (工单ID -> 工单)
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, WorkOrder>> workOrderCache = new ConcurrentHashMap<>();
    // 派工单ID -> 操作记录
    private final ConcurrentHashMap<String, List<OperateRecord>> operateRecordCache = new ConcurrentHashMap<>();

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
    @Resource
    private OperateRecordMapper operateRecordMapper;

    @Override
    public Page<TaskOrderVO> searchByPage(TaskOrderQueryDTO param) {
        return taskOrderMapper.selectJoinPage(param.getPage(), TaskOrderVO.class,
                new MPJLambdaWrapper<TaskOrder>()
                        .innerJoin(Order.class, Order::getId, TaskOrder::getOrderId)
                        .innerJoin(WorkOrder.class, WorkOrder::getId, TaskOrder::getWorkOrderId)
                        .innerJoin(Product.class, Product::getId, TaskOrder::getProductId)
                        .innerJoin(Bom.class, Bom::getId, TaskOrder::getBomId)
                        .innerJoin(Routing.class, Routing::getId, TaskOrder::getRoutingId)
                        .innerJoin(Operation.class, Operation::getId, TaskOrder::getOperationId)
                        // 查询条件
                        .like(StrUtil.isNotBlank(param.getOrderCode()), Order::getOrderCode, param.getOrderCode())
                        .like(StrUtil.isNotBlank(param.getWorkOrderCode()), WorkOrder::getWorkOrderCode, param.getWorkOrderCode())
                        .like(StrUtil.isNotBlank(param.getTaskOrderCode()), TaskOrder::getTaskOrderCode, param.getTaskOrderCode())
                        .eq(Objects.nonNull(param.getStatus()) && !OrderStatus.ALL.equals(param.getStatus()), TaskOrder::getStatus, param.getStatus())
                        .between(Objects.nonNull(param.getActualBeginTime()) && Objects.nonNull(param.getActualEndTime()),
                                TaskOrder::getActualEndTime, param.getActualBeginTime(), param.getActualEndTime())
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
    public Page<TaskOrderVO> searchByPageEx(TaskOrderQueryDTO param) {
        Page<TaskOrder> taskOrderPage = taskOrderMapper.selectPage(new Page<>(param.getCurrent(), param.getSize()), new LambdaQueryWrapper<TaskOrder>()
                // 查询条件
                .like(StrUtil.isNotBlank(param.getTaskOrderCode()), TaskOrder::getTaskOrderCode, param.getTaskOrderCode())
                .eq(Objects.nonNull(param.getStatus()) && !OrderStatus.ALL.equals(param.getStatus()), TaskOrder::getStatus, param.getStatus())
                // TODO 这个条件只能查询出近一个月报工的派工单数据
//                .between(Objects.nonNull(param.getActualBeginTime()) && Objects.nonNull(param.getActualEndTime()), TaskOrder::getActualEndTime, param.getActualBeginTime(), param.getActualEndTime())
                // 查询近一个月数据比较合理
                .between(TaskOrder::getCreateTime, param.getActualBeginTime(), param.getActualEndTime())
                // 查询字段
                .select(TaskOrder::getId)
                // 排序
                .orderByDesc(TaskOrder::getCreateTime));
        Page<TaskOrderVO> pageResult = new Page<>();
        BeanUtils.copyProperties(taskOrderPage, pageResult);
        List<TaskOrder> records = taskOrderPage.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            List<String> taskOrderIds = records.stream().map(TaskOrder::getId).collect(Collectors.toList());
            List<TaskOrderVO> dataList = taskOrderMapper.findTaskDetailByIds(taskOrderIds);
            pageResult.setRecords(dataList);
        }

        return pageResult;
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
        TaskOrder taskOrder = getTaskOrderCache(taskOrderId);
        if(Objects.isNull(taskOrder)) {
            taskOrder = taskOrderMapper.selectOne(new LambdaQueryWrapper<TaskOrder>()
                    .select(TaskOrder::getId, TaskOrder::getWorkOrderId, TaskOrder::getOrderId, TaskOrder::getStatus, TaskOrder::getSort)
                    .eq(TaskOrder::getId, taskOrderId));
        }
        if (Objects.isNull(taskOrder)) {
            throw new BusinessException("派工单不存在！");
        }

        TaskOrder preTaskOrder = getPreTaskOrderByCache(taskOrder.getOrderId(), taskOrder.getWorkOrderId(), taskOrder.getSort());
        if (Objects.nonNull(preTaskOrder) && !OrderStatus.REPORT.equals(preTaskOrder.getStatus())) {
            log.error("派工单【{}】开工失败，上道工序未报工！", taskOrder.getId());
            throw new BusinessException("派工单开工失败，上道工序未报工！");
        }

        if (!OrderStatus.INIT.equals(taskOrder.getStatus())) {
            StringBuilder buffer = new StringBuilder("派工单开工失败，");
            switch (taskOrder.getStatus()) {
                case START:
                    buffer.append("派工单已开工!派工单ID：").append(taskOrder.getId());
                    break;
                case PAUSE:
                    buffer.append("派工单已暂停!派工单ID：").append(taskOrder.getId());
                    break;
                case RESUME:
                    buffer.append("派工单已复工!派工单ID：").append(taskOrder.getId());
                    break;
                case REPORT:
                    buffer.append("派工单已报工!派工单ID：").append(taskOrder.getId());
                    break;
                default:
                    buffer.append("派工单未知状态!派工单ID：").append(taskOrder.getId());
                    break;
            }
            throw new BusinessException(buffer.toString());
        }

        // 更新工单状态(工单下派工单全部为初始化，可开工)
        if (checkWorkOrderStart(taskOrder.getOrderId(), taskOrder.getWorkOrderId())) {
            // 当实际开工时间为null时，设置工单实际开工时间
            workOrderMapper.update(new LambdaUpdateWrapper<WorkOrder>()
                    .set(WorkOrder::getOrderStatus, OrderStatus.START)
                    .set(WorkOrder::getActualBeginTime, taskOrder.getActualBeginTime())
                    .eq(WorkOrder::getId, taskOrder.getWorkOrderId())
                    .isNull(WorkOrder::getActualBeginTime));
        }

        // 更新订单状态(订单下工单全部为初始状态，订单可开工)
        if (checkOrderStart(taskOrder.getOrderId())) {
            // 当实际开工时间为null时，设置订单实际开工时间
            orderMapper.update(new LambdaUpdateWrapper<Order>()
                    .set(Order::getOrderStatus, OrderStatus.START)
                    .set(Order::getActualBeginTime, taskOrder.getActualBeginTime())
                    .eq(Order::getId, taskOrder.getOrderId())
                    .isNull(Order::getActualBeginTime));
        }

        // 更新派工单状态
        taskOrder.setStatus(OrderStatus.START);
        taskOrder.setActualBeginTime(new Date());
        taskOrderMapper.updateById(taskOrder);

        // 更新派工单状态，后续操作要用
        updateTaskOrderCache(taskOrder.getOrderId(), taskOrder.getWorkOrderId(), taskOrder.getId(), taskOrder);

        // 创建开工记录
//        operateRecordService.createRecord(OperateType.START, taskOrder.getId());
        List<OperateRecord> operateRecordList = operateRecordCache.computeIfAbsent(taskOrderId, v -> new ArrayList<>());
        operateRecordList.add(operateRecordService.generateRecord(OperateType.START, taskOrder.getId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pauseWork(String taskOrderId) {
        TaskOrder taskOrder = getTaskOrderCache(taskOrderId);
        if(Objects.isNull(taskOrder)) {
            taskOrder = taskOrderMapper.selectOne(new LambdaQueryWrapper<TaskOrder>()
                    .select(TaskOrder::getId, TaskOrder::getWorkOrderId, TaskOrder::getOrderId, TaskOrder::getStatus)
                    .eq(TaskOrder::getId, taskOrderId));
        }
        if (Objects.isNull(taskOrder)) {
            throw new BusinessException("派工单不存在！");
        }
        if (!OrderStatus.START.equals(taskOrder.getStatus())
                && !OrderStatus.RESUME.equals(taskOrder.getStatus())) {
            StringBuilder buffer = new StringBuilder("派工单暂停失败，");
            switch (taskOrder.getStatus()) {
                case INIT:
                    buffer.append("派工单未开工!派工单ID：").append(taskOrder.getId());
                    break;
                case PAUSE:
                    buffer.append("派工单已暂停!派工单ID：").append(taskOrder.getId());
                    break;
                case REPORT:
                    buffer.append("派工单已报工!派工单ID：").append(taskOrder.getId());
                    break;
                default:
                    buffer.append("派工单未知状态!派工单ID：").append(taskOrder.getId());
                    break;
            }
            throw new BusinessException(buffer.toString());
        }
        // 更新派工单状态
        taskOrder.setStatus(OrderStatus.PAUSE);
        taskOrderMapper.updateById(taskOrder);

        // 更新派工单状态，后续操作要用
        updateTaskOrderCache(taskOrder.getOrderId(), taskOrder.getWorkOrderId(), taskOrder.getId(), taskOrder);

        // 更新工单状态
//        WorkOrder workOrder = workOrderMapper.selectById(taskOrder.getWorkOrderId());
//        workOrder.setOrderStatus(OrderStatus.PAUSE);
//        workOrderMapper.updateById(workOrder);
        if(checkWorkOrderPause(taskOrder.getOrderId(), taskOrder.getWorkOrderId())) {
            workOrderMapper.update(
                    new LambdaUpdateWrapper<WorkOrder>()
                            .set(WorkOrder::getOrderStatus, OrderStatus.PAUSE)
                            .eq(WorkOrder::getId, taskOrder.getWorkOrderId())
            );
        }

        // 更新订单状态
//        Order order = orderMapper.selectById(taskOrder.getOrderId());
//        order.setOrderStatus(OrderStatus.PAUSE);
//        orderMapper.updateById(order);
        if(checkOrderPause(taskOrder.getOrderId())) {
            orderMapper.update(
                    new LambdaUpdateWrapper<Order>()
                            .set(Order::getOrderStatus, OrderStatus.PAUSE)
                            .eq(Order::getId, taskOrder.getOrderId())
            );
        }

        // 创建暂停记录
//        operateRecordService.createRecord(OperateType.PAUSE, taskOrder.getId());
        List<OperateRecord> operateRecordList = operateRecordCache.computeIfAbsent(taskOrderId, v -> new ArrayList<>());
        operateRecordList.add(operateRecordService.generateRecord(OperateType.PAUSE, taskOrder.getId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resumeWork(String taskOrderId) {
        TaskOrder taskOrder = getTaskOrderCache(taskOrderId);
        if(Objects.isNull(taskOrder)) {
            taskOrder = taskOrderMapper.selectOne(new LambdaQueryWrapper<TaskOrder>()
                    .select(TaskOrder::getId, TaskOrder::getWorkOrderId, TaskOrder::getOrderId, TaskOrder::getStatus)
                    .eq(TaskOrder::getId, taskOrderId));
        }
        if (Objects.isNull(taskOrder)) {
            throw new BusinessException("派工单不存在！");
        }
        if (!OrderStatus.PAUSE.equals(taskOrder.getStatus())) {
            StringBuilder buffer = new StringBuilder("派工单复工失败，");
            switch (taskOrder.getStatus()) {
                case INIT:
                    buffer.append("派工单未开工!派工单ID：").append(taskOrder.getId());
                    break;
                case START:
                    buffer.append("派工单已开工!派工单ID：").append(taskOrder.getId());
                    break;
                case RESUME:
                    buffer.append("派工单已复工!派工单ID：").append(taskOrder.getId());
                    break;
                case REPORT:
                    buffer.append("派工单已报工!派工单ID：").append(taskOrder.getId());
                    break;
                default:
                    buffer.append("派工单未知状态!派工单ID：").append(taskOrder.getId());
                    break;
            }
            throw new BusinessException(buffer.toString());
        }
        // 更新派工单状态
        taskOrder.setStatus(OrderStatus.RESUME);
        taskOrderMapper.updateById(taskOrder);

        // 更新派工单状态，后续操作要用
        updateTaskOrderCache(taskOrder.getOrderId(), taskOrder.getWorkOrderId(), taskOrder.getId(), taskOrder);

        // 更新工单状态
//        WorkOrder workOrder = workOrderMapper.selectById(taskOrder.getWorkOrderId());
//        workOrder.setOrderStatus(OrderStatus.RESUME);
//        workOrderMapper.updateById(workOrder);
        if(checkWorkOrderResume(taskOrder.getOrderId(), taskOrder.getWorkOrderId())) {
            workOrderMapper.update(
                    new LambdaUpdateWrapper<WorkOrder>()
                            .set(WorkOrder::getOrderStatus, OrderStatus.RESUME)
                            .eq(WorkOrder::getId, taskOrder.getWorkOrderId())
            );
        }

        // 更新订单状态
//        Order order = orderMapper.selectById(taskOrder.getOrderId());
//        order.setOrderStatus(OrderStatus.RESUME);
//        orderMapper.updateById(order);
        if(checkOrderResume(taskOrder.getOrderId())) {
            orderMapper.update(
                    new LambdaUpdateWrapper<Order>()
                            .set(Order::getOrderStatus, OrderStatus.RESUME)
                            .eq(Order::getId, taskOrder.getOrderId())
            );
        }

        // 创建复工记录
//        operateRecordService.createRecord(OperateType.RESUME, taskOrder.getId());
        List<OperateRecord> operateRecordList = operateRecordCache.computeIfAbsent(taskOrderId, v -> new ArrayList<>());
        operateRecordList.add(operateRecordService.generateRecord(OperateType.RESUME, taskOrder.getId()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reportWork(String taskOrderId) {
        TaskOrder taskOrder = getTaskOrderCache(taskOrderId);
        if(Objects.isNull(taskOrder)) {
            taskOrder = taskOrderMapper.selectOne(new LambdaQueryWrapper<TaskOrder>()
                    .select(TaskOrder::getId, TaskOrder::getWorkOrderId, TaskOrder::getOrderId, TaskOrder::getStatus)
                    .eq(TaskOrder::getId, taskOrderId));
        }
        if (Objects.isNull(taskOrder)) {
            throw new BusinessException("派工单不存在！");
        }
        if (!OrderStatus.START.equals(taskOrder.getStatus())
                && !OrderStatus.RESUME.equals(taskOrder.getStatus())) {
            StringBuilder buffer = new StringBuilder("派工单报工失败，");
            switch (taskOrder.getStatus()) {
                case INIT:
                    buffer.append("派工单未开工!派工单ID：").append(taskOrder.getId());
                    break;
                case PAUSE:
                    buffer.append("派工单已暂停!派工单ID：").append(taskOrder.getId());
                    break;
                case REPORT:
                    buffer.append("派工单已报工!派工单ID：").append(taskOrder.getId());
                    break;
                default:
                    buffer.append("派工单未知状态!派工单ID：").append(taskOrder.getId());
                    break;
            }
            throw new BusinessException(buffer.toString());
        }
        // 更新派工单状态
        taskOrder.setStatus(OrderStatus.REPORT);
        taskOrder.setActualEndTime(new Date());
        taskOrderMapper.updateById(taskOrder);

        // 更新派工单状态，后续操作要用
        updateTaskOrderCache(taskOrder.getOrderId(), taskOrder.getWorkOrderId(), taskOrder.getId(), taskOrder);

        // 更新工单状态
        if (checkWorkOrderReport(taskOrder.getOrderId(), taskOrder.getWorkOrderId())) {
//            WorkOrder workOrder = workOrderMapper.selectById(taskOrder.getWorkOrderId());
//            workOrder.setOrderStatus(OrderStatus.REPORT);
//            workOrder.setActualEndTime(taskOrder.getActualEndTime());
//            workOrderMapper.updateById(workOrder);
            workOrderMapper.update(
                    new LambdaUpdateWrapper<WorkOrder>()
                            // 只更新需要的两个字段
                            .set(WorkOrder::getOrderStatus, OrderStatus.REPORT)
                            .set(WorkOrder::getActualEndTime, taskOrder.getActualEndTime())
                            .eq(WorkOrder::getId, taskOrder.getWorkOrderId())
            );

            // 清空缓存，释放内存
            removeWorkOrderCache(taskOrder.getOrderId(), taskOrder.getWorkOrderId());

            // 工单完工，生成质检单
            applicationContext.publishEvent(new CheckOrderEvent(this, taskOrder.getWorkOrderId()));
        }

        // 更新订单状态
        if (checkOrderReport(taskOrder.getOrderId())) {
//            Order order = orderMapper.selectById(taskOrder.getOrderId());
//            order.setOrderStatus(OrderStatus.REPORT);
//            order.setActualEndTime(taskOrder.getActualEndTime());
//            orderMapper.updateById(order);

            orderMapper.update(
                    new LambdaUpdateWrapper<Order>()
                            .set(Order::getOrderStatus, OrderStatus.REPORT)
                            .set(Order::getActualEndTime, taskOrder.getActualEndTime())
                            .eq(Order::getId, taskOrder.getOrderId())
            );
        }

        // 创建报工记录
//        operateRecordService.createRecord(OperateType.REPORT, taskOrder.getId());
        List<OperateRecord> operateRecordList = operateRecordCache.computeIfAbsent(taskOrderId, v -> new ArrayList<>());
        operateRecordList.add(operateRecordService.generateRecord(OperateType.REPORT, taskOrder.getId()));

        List<OperateRecord> recordList = operateRecordCache.remove(taskOrderId);
        if(!CollectionUtils.isEmpty(recordList)) {
            operateRecordMapper.insertBatch(recordList);
        }

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
    public TaskOrder getPreTaskOrder(String workOrderId, Integer currentSort) {
        LambdaQueryWrapper<TaskOrder> wrapper = new LambdaQueryWrapper<TaskOrder>();
        wrapper.select(TaskOrder::getId, TaskOrder::getStatus)
                .eq(TaskOrder::getWorkOrderId, workOrderId)
                .lt(TaskOrder::getSort, currentSort)
                .orderByDesc(TaskOrder::getSort)
                .last("LIMIT 1"); // PostgreSQL 用 LIMIT 1
        return taskOrderMapper.selectOne(wrapper);
    }

    @Override
    public TaskOrder getPreTaskOrderByCache(String orderId, String workOrderId, Integer currentSort) {
        // 只遍历一次缓存获取工单下全部派工单
        ConcurrentHashMap<String, TaskOrder> taskMap = getTaskOrderMap(orderId, workOrderId);
        // 缓存不存在该工单，降级走数据库
        if (taskMap == null || taskMap.isEmpty()) {
            LambdaQueryWrapper<TaskOrder> wrapper = new LambdaQueryWrapper<TaskOrder>();
            wrapper.select(TaskOrder::getId, TaskOrder::getStatus, TaskOrder::getSort)
                    .eq(TaskOrder::getWorkOrderId, workOrderId)
                    .lt(TaskOrder::getSort, currentSort)
                    .orderByDesc(TaskOrder::getSort)
                    .last("LIMIT 1");
            return taskOrderMapper.selectOne(wrapper);
        }

        // 在内存中筛选 sort < currentSort 且sort最大的工序
        TaskOrder target = null;
        int maxSortVal = -1;
        for (TaskOrder task : taskMap.values()) {
            Integer sort = task.getSort();
            if (sort != null && sort < currentSort && sort > maxSortVal) {
                maxSortVal = sort;
                target = task;
            }
        }
        return target;
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
        // 1.查询缓存
        ConcurrentHashMap<String, WorkOrder> workOrderMap = getWorkOrderMap(orderId);
        // 校验缓存派工单状态
        if(!CollectionUtils.isEmpty(workOrderMap)) {
            for (WorkOrder workOrder : workOrderMap.values()) {
                if(!workOrder.getOrderStatus().equals(OrderStatus.INIT)) {
                    return false;
                }
            }
            return true;
        }
        // 2.查询数据库
        List<WorkOrder> workOrderList = workOrderMapper.selectList(new LambdaQueryWrapper<WorkOrder>()
                .select(WorkOrder::getId, WorkOrder::getOrderId, WorkOrder::getOrderStatus)
                .eq(WorkOrder::getOrderId, orderId));

        // 回填缓存
        batchUpdateWorkOrderCache(orderId, workOrderList);

        long initCount = workOrderList.stream().filter(workOrder -> OrderStatus.INIT.equals(workOrder.getOrderStatus())).count();

//        // 查询初始状态工单数量
//        Long initCount = workOrderMapper.selectCount(new LambdaQueryWrapper<WorkOrder>()
//                .eq(WorkOrder::getOrderId, orderId)
//                .eq(WorkOrder::getOrderStatus, OrderStatus.INIT));
//        // 查询工单总数量
//        Long workOrderCount = workOrderMapper.selectCount(new LambdaQueryWrapper<WorkOrder>()
//                .eq(WorkOrder::getOrderId, orderId));
//        return initCount.equals(workOrderCount);

        return initCount == workOrderList.size();
    }

    @Override
    public boolean checkOrderReport(String orderId) {
        // 1.查询缓存
        ConcurrentHashMap<String, WorkOrder> workOrderMap = getWorkOrderMap(orderId);
        // 校验缓存派工单状态
        if(!CollectionUtils.isEmpty(workOrderMap)) {
            for (WorkOrder workOrder : workOrderMap.values()) {
                if(!workOrder.getOrderStatus().equals(OrderStatus.REPORT)) {
                    return false;
                }
            }
            return true;
        }

        // 2.查询数据库
        List<WorkOrder> workOrderList = workOrderMapper.selectList(new LambdaQueryWrapper<WorkOrder>()
                .select(WorkOrder::getId, WorkOrder::getOrderId, WorkOrder::getOrderStatus));

        // 回填缓存
        batchUpdateWorkOrderCache(orderId, workOrderList);

        long reportCount = workOrderList.stream().filter(workOrder -> OrderStatus.REPORT.equals(workOrder.getOrderStatus())).count();

//        // 查询报工状态工单数量
//        Long reportCount = workOrderMapper.selectCount(new LambdaQueryWrapper<WorkOrder>()
//                .eq(WorkOrder::getOrderId, orderId)
//                .eq(WorkOrder::getOrderStatus, OrderStatus.REPORT));
//        // 查询工单总数量
//        Long workOrderCount = workOrderMapper.selectCount(new LambdaQueryWrapper<WorkOrder>()
//                .eq(WorkOrder::getOrderId, orderId));
//        return reportCount.equals(workOrderCount);

        return reportCount == workOrderList.size();
    }

    @Override
    public boolean checkOrderPause(String orderId) {
        // 1.查询缓存
        ConcurrentHashMap<String, WorkOrder> workOrderMap = getWorkOrderMap(orderId);
        // 校验缓存派工单状态
        if(!CollectionUtils.isEmpty(workOrderMap)) {
            for (WorkOrder workOrder : workOrderMap.values()) {
                if(workOrder.getOrderStatus().equals(OrderStatus.PAUSE)) {
                    return true;
                }
            }
            return false;
        }

        // 2.查询数据库
        List<WorkOrder> workOrderList = workOrderMapper.selectList(new LambdaQueryWrapper<WorkOrder>()
                .select(WorkOrder::getId, WorkOrder::getOrderId, WorkOrder::getOrderStatus));

        // 回填缓存
        batchUpdateWorkOrderCache(orderId, workOrderList);

        for (WorkOrder workOrder : workOrderList) {
            if(workOrder.getOrderStatus().equals(OrderStatus.PAUSE)) {
                return true;
            }
        }

//        return workOrderMapper.exists(new LambdaQueryWrapper<WorkOrder>()
//                .eq(WorkOrder::getOrderId, orderId)
//                .eq(WorkOrder::getOrderStatus, OrderStatus.PAUSE));

        return false;
    }

    @Override
    public boolean checkOrderResume(String orderId) {
        // 1.查询缓存
        ConcurrentHashMap<String, WorkOrder> workOrderMap = getWorkOrderMap(orderId);
        // 校验缓存派工单状态
        if(!CollectionUtils.isEmpty(workOrderMap)) {
            int reportCount = 0;
            for (WorkOrder workOrder : workOrderMap.values()) {
                // 存在初始、暂停派工单，不复工
                if(workOrder.getOrderStatus().equals(OrderStatus.INIT) || workOrder.getOrderStatus().equals(OrderStatus.PAUSE)) {
                    return false;
                }
                if(workOrder.getOrderStatus().equals(OrderStatus.REPORT)) {
                    reportCount++;
                }
            }
            // 全部报工，不复工
            if(reportCount == workOrderMap.size()) {
                return false;
            }
            return true;
        }

        // 2.查询数据库
        List<WorkOrder> workOrderList = workOrderMapper.selectList(new LambdaQueryWrapper<WorkOrder>()
                .select(WorkOrder::getId, WorkOrder::getOrderId, WorkOrder::getOrderStatus));

        // 回填缓存
        batchUpdateWorkOrderCache(orderId, workOrderList);

        int reportCount = 0;
        for (WorkOrder workOrder : workOrderList) {
            // 存在初始、暂停派工单，不复工
            if(workOrder.getOrderStatus().equals(OrderStatus.INIT) || workOrder.getOrderStatus().equals(OrderStatus.PAUSE)) {
                return false;
            }
            if(workOrder.getOrderStatus().equals(OrderStatus.REPORT)) {
                reportCount++;
            }
        }
        // 全部报工，不复工
        if(reportCount == workOrderMap.size()) {
            return false;
        }

//        // 查询初始、暂停工单数量
//        Long initPauseCount = workOrderMapper.selectCount(new LambdaQueryWrapper<WorkOrder>()
//                .eq(WorkOrder::getOrderId, orderId)
//                .in(WorkOrder::getOrderStatus, OrderStatus.INIT, OrderStatus.PAUSE));
//        if(initPauseCount > 0) {
//            return false;
//        }
//        // 查询报工状态工单数量
//        Long reportCount = workOrderMapper.selectCount(new LambdaQueryWrapper<WorkOrder>()
//                .eq(WorkOrder::getOrderId, orderId)
//                .eq(WorkOrder::getOrderStatus, OrderStatus.REPORT));
//        // 查询工单总数量
//        Long workOrderCount = workOrderMapper.selectCount(new LambdaQueryWrapper<WorkOrder>()
//                .eq(WorkOrder::getOrderId, orderId));
//        return !Objects.equals(reportCount, workOrderCount);

        return true;
    }

    @Override
    public boolean checkWorkOrderStart(String orderId, String workOrderId) {
        // 1.查找缓存
        ConcurrentHashMap<String, TaskOrder> taskOrderMap = getTaskOrderMap(orderId, workOrderId);
        // 校验缓存派工单状态
        if(!CollectionUtils.isEmpty(taskOrderMap)) {
            for (TaskOrder taskOrder : taskOrderMap.values()) {
                if(!taskOrder.getStatus().equals(OrderStatus.INIT)) {
                    return false;
                }
            }
            return true;
        }
        // 2.查询数据库
        List<TaskOrder> taskOrderList = taskOrderMapper.selectList(new LambdaQueryWrapper<TaskOrder>()
                .select(TaskOrder::getId, TaskOrder::getOrderId, TaskOrder::getWorkOrderId, TaskOrder::getStatus, TaskOrder::getSort)
                .eq(TaskOrder::getWorkOrderId, workOrderId));
        // 回填缓存
        batchUpdateTaskOrderCache(orderId, workOrderId, taskOrderList);
        // 判断派工单总数和初始化派工单总数是否相同，相同，工单可以开工，不相同，工单不可开工
        long initCount = taskOrderList.stream().filter(taskOrder -> OrderStatus.INIT.equals(taskOrder.getStatus())).count();
        int taskCount = taskOrderList.size();
        return initCount == taskCount;
    }

    @Override
    public boolean checkWorkOrderReport(String orderId, String workOrderId) {
        // 1.查找缓存
        ConcurrentHashMap<String, TaskOrder> taskOrderMap = getTaskOrderMap(orderId, workOrderId);
        // 校验缓存派工单状态
        if(!CollectionUtils.isEmpty(taskOrderMap)) {
            for (TaskOrder taskOrder : taskOrderMap.values()) {
                if(!taskOrder.getStatus().equals(OrderStatus.REPORT)) {
                    return false;
                }
            }
            return true;
        }
        // 2.查询数据库
        List<TaskOrder> taskOrderList = taskOrderMapper.selectList(new LambdaQueryWrapper<TaskOrder>()
                .select(TaskOrder::getId, TaskOrder::getOrderId, TaskOrder::getWorkOrderId, TaskOrder::getStatus, TaskOrder::getSort)
                .eq(TaskOrder::getWorkOrderId, workOrderId));
        // 回填缓存
        batchUpdateTaskOrderCache(orderId, workOrderId, taskOrderList);
        // 判断派工单总数和报工派工单总数是否相同，相同，工单可以报工，不相同，工单不可报工
        long reportCount = taskOrderList.stream().filter(taskOrder -> OrderStatus.REPORT.equals(taskOrder.getStatus())).count();
        int taskCount = taskOrderList.size();
        return reportCount == taskCount;
    }

    @Override
    public boolean checkWorkOrderPause(String orderId, String workOrderId) {
        // 1.查找缓存
        ConcurrentHashMap<String, TaskOrder> taskOrderMap = getTaskOrderMap(orderId, workOrderId);
        // 校验缓存派工单状态
        if(!CollectionUtils.isEmpty(taskOrderMap)) {
            for (TaskOrder taskOrder : taskOrderMap.values()) {
                if(taskOrder.getStatus().equals(OrderStatus.PAUSE)) {
                    return true;
                }
            }
            return false;
        }
        // 2.查询数据库
        List<TaskOrder> taskOrderList = taskOrderMapper.selectList(new LambdaQueryWrapper<TaskOrder>()
                .select(TaskOrder::getId, TaskOrder::getOrderId, TaskOrder::getWorkOrderId, TaskOrder::getStatus, TaskOrder::getSort)
                .eq(TaskOrder::getWorkOrderId, workOrderId));
        // 回填缓存
        batchUpdateTaskOrderCache(orderId, workOrderId, taskOrderList);
        // 判断暂停派工单数量是否大于0，大于0，可以暂停，等于0，不可暂停
        for (TaskOrder taskOrder : taskOrderList) {
            if(taskOrder.getStatus().equals(OrderStatus.PAUSE)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkWorkOrderResume(String orderId, String workOrderId) {
        // 1.查找缓存
        ConcurrentHashMap<String, TaskOrder> taskOrderMap = getTaskOrderMap(orderId, workOrderId);
        // 校验缓存派工单状态
        if(!CollectionUtils.isEmpty(taskOrderMap)) {
            int reportCount = 0;
            for (TaskOrder taskOrder : taskOrderMap.values()) {
                // 有初始、暂停派工单，不能复工
                if(taskOrder.getStatus().equals(OrderStatus.INIT) || taskOrder.getStatus().equals(OrderStatus.PAUSE)) {
                    return false;
                }
                if(taskOrder.getStatus().equals(OrderStatus.REPORT)) {
                    reportCount++;
                }
            }
            // 全部报工，不能复工
            if(reportCount == taskOrderMap.size()) {
                return false;
            }
            return true;
        }
        // 2.查询数据库
        List<TaskOrder> taskOrderList = taskOrderMapper.selectList(new LambdaQueryWrapper<TaskOrder>()
                .select(TaskOrder::getId, TaskOrder::getOrderId, TaskOrder::getWorkOrderId, TaskOrder::getStatus, TaskOrder::getSort)
                .eq(TaskOrder::getWorkOrderId, workOrderId));
        // 回填缓存
        batchUpdateTaskOrderCache(orderId, workOrderId, taskOrderList);

        int reportCount = 0;
        for (TaskOrder taskOrder : taskOrderList) {
            // 有初始、暂停派工单，不能复工
            if(taskOrder.getStatus().equals(OrderStatus.INIT) || taskOrder.getStatus().equals(OrderStatus.PAUSE)) {
                return false;
            }
            if(taskOrder.getStatus().equals(OrderStatus.REPORT)) {
                reportCount++;
            }
        }
        // 全部报工，不能复工
        return reportCount != taskOrderList.size();
    }

    /**
     * 获取派工单缓存
     * @param orderId
     * @param workOrderId
     * @return
     */
    private ConcurrentHashMap<String, TaskOrder> getTaskOrderMap(String orderId, String workOrderId) {
        // 不存在则创建订单层map
        ConcurrentHashMap<String, ConcurrentHashMap<String, TaskOrder>> workOrderMap =
                taskOrderCache.computeIfAbsent(orderId, v -> new ConcurrentHashMap<>());
        // 不存在则创建工单层taskMap
        return workOrderMap.computeIfAbsent(workOrderId, v -> new ConcurrentHashMap<>());
    }

    /**
     * 获取工单缓存
     * @param orderId
     * @return
     */
    private ConcurrentHashMap<String, WorkOrder> getWorkOrderMap(String orderId) {
        // 不存在则创建订单层map
        return workOrderCache.computeIfAbsent(orderId, v -> new ConcurrentHashMap<>());
    }

    /**
     * 批量缓存回填工具
     * @param orderId
     * @param workOrderId
     * @param taskOrderList
     */
    private void batchUpdateTaskOrderCache(String orderId, String workOrderId, List<TaskOrder> taskOrderList) {
        ConcurrentHashMap<String, TaskOrder> taskMap = getTaskOrderMap(orderId, workOrderId);
        taskMap.clear();
        for (TaskOrder task : taskOrderList) {
            taskMap.put(task.getId(), task);
        }
    }

    /**
     * 批量缓存回填工具
     * @param orderId
     * @param workOrderList
     */
    private void batchUpdateWorkOrderCache(String orderId, List<WorkOrder> workOrderList) {
        ConcurrentHashMap<String, WorkOrder> woMap = getWorkOrderMap(orderId);
        woMap.clear();
        for (WorkOrder wo : workOrderList) {
            woMap.put(wo.getId(), wo);
        }
    }

    @Override
    public long getTodayFinishTaskOrderCount() {
        return taskOrderMapper.selectJoinCount(new MPJLambdaWrapper<TaskOrder>()
                .innerJoin(WorkOrder.class, WorkOrder::getId, TaskOrder::getWorkOrderId)
                .innerJoin(Order.class, Order::getId, TaskOrder::getOrderId)
                .eq(TaskOrder::getStatus, OrderStatus.REPORT)
                .between(TaskOrder::getActualEndTime, DateUtils.getBeginTimeOfToday(), DateUtils.getEndTimeOfToday()));
    }

    @Override
    public List<DailyProcessFinishTaskOrderQtyBO> queryDailyProcessFinishTaskOrderQty() {
        return taskOrderMapper.queryDailyProcessFinishTaskOrderQty();
    }

    @Override
    public DailyTaskOrderVO countDailyTaskOrder() {
        Date now = new Date();
        List<DailyTaskOrderBO> dailyTaskOrderBOList = taskOrderMapper.queryDailyTaskOrder(DateUtil.beginOfMonth(now), DateUtil.endOfMonth(now));
        DailyTaskOrderVO dailyTaskOrderVO = new DailyTaskOrderVO();
        List<String> xAxis = new ArrayList<>(dailyTaskOrderBOList.size());
        List<Long> yAxis = new ArrayList<>(dailyTaskOrderBOList.size());
        for (DailyTaskOrderBO dailyTaskOrderBO : dailyTaskOrderBOList) {
            xAxis.add(dailyTaskOrderBO.getDaily());
            yAxis.add(dailyTaskOrderBO.getFinishQty());
        }
        dailyTaskOrderVO.setXAxis(xAxis);
        dailyTaskOrderVO.setYAxis(yAxis);
        return dailyTaskOrderVO;
    }

    @Override
    public DailyOpTaskOrderVO countDailyOpTaskOrder() {
        Date now = new Date();
        List<DailyTaskOrderBO> dailyTaskOrderBOList = taskOrderMapper.queryDailyOpTaskOrder(DateUtil.beginOfMonth(now), DateUtil.endOfMonth(now));
        // 空列表快速返回，避免后续空指针
        if (CollectionUtils.isEmpty(dailyTaskOrderBOList)) {
            DailyOpTaskOrderVO emptyVO = new DailyOpTaskOrderVO();
            emptyVO.setXAxis(new ArrayList<>());
            emptyVO.setYAxis(new ArrayList<>());
            return emptyVO;
        }


        // 双层分组：Map<日期, Map<工序编码, 完工数量总和>>
        Map<String, Map<String, Long>> dailyOpTaskMap = dailyTaskOrderBOList.stream()
                .collect(Collectors.groupingBy(
                        DailyTaskOrderBO::getDaily,  // 第一层：按日期分组
                        TreeMap::new,                // 关键：指定外层Map为TreeMap（天然按Key升序）
                        Collectors.groupingBy(
                                DailyTaskOrderBO::getOpCode,  // 第二层：按工序编码分组
                                Collectors.summingLong(DailyTaskOrderBO::getFinishQty)
                        )
                ));

        // 查找最大工序个数
        TreeSet<String> allOpCodes = dailyOpTaskMap.values().stream()
                .flatMap(opTaskMap -> opTaskMap.keySet().stream())
                .collect(Collectors.toCollection(TreeSet::new));
        Map<String, Integer> opCodeIndex = new HashMap<>(); // 核心优化：O(1)查找工序索引
        int idx = 0;
        for (String op : allOpCodes) opCodeIndex.put(op, idx++);
        int opCount = allOpCodes.size();

        // 初始化轴
        DailyOpTaskOrderVO dailyTaskOrderVO = new DailyOpTaskOrderVO();
        List<String> xAxis = new ArrayList<>(dailyOpTaskMap.keySet());
        List<List<Long>> yAxis = new ArrayList<>();
        for (int i = 0; i < dailyOpTaskMap.keySet().size(); i++) {
            yAxis.add(new ArrayList<>(Collections.nCopies(opCount, 0L)));
        }

        // 填充数据（简化遍历，去掉indexOf）
        for (int dateIdx = 0; dateIdx < xAxis.size(); dateIdx++) {
            String daily = xAxis.get(dateIdx);
            Map<String, Long> opTaskMap = dailyOpTaskMap.get(daily);
            if (opTaskMap == null) continue;
            // 遍历工序，填充数值（空值补0）
            for (Map.Entry<String, Long> entry : opTaskMap.entrySet()) {
                Integer opIdx = opCodeIndex.get(entry.getKey());
                if (opIdx != null) {
                    yAxis.get(dateIdx).set(opIdx, Optional.ofNullable(entry.getValue()).orElse(0L));
                }
            }
        }

        dailyTaskOrderVO.setXAxis(xAxis);
        dailyTaskOrderVO.setYAxis(yAxis);
        return dailyTaskOrderVO;
    }

    public DailyOpTaskOrderVO countDailyOpTaskOrderEx() {
        // ========== 1. 基础数据获取 & 空值防御 ==========
        Date now = new Date();
        List<DailyTaskOrderBO> dailyTaskOrderBOList = taskOrderMapper.queryDailyOpTaskOrder(
                DateUtil.beginOfMonth(now),
                DateUtil.endOfMonth(now)
        );
        if (CollectionUtils.isEmpty(dailyTaskOrderBOList)) {
            DailyOpTaskOrderVO emptyVO = new DailyOpTaskOrderVO();
            emptyVO.setXAxis(new ArrayList<>());
            emptyVO.setYAxis(new ArrayList<>());
            return emptyVO;
        }

        // ========== 2. 双层分组（日期升序，避免无序） ==========
        // 外层：日期(String) → 内层：工序编码(String) → 完工数量(Long)
        Map<String, Map<String, Long>> dailyOpTaskMap = dailyTaskOrderBOList.stream()
                .collect(Collectors.groupingBy(
                        DailyTaskOrderBO::getDaily,
                        TreeMap::new,  // 日期天然按yyyy-MM-dd升序，无需额外排序
                        Collectors.groupingBy(
                                DailyTaskOrderBO::getOpCode,
                                Collectors.summingLong(DailyTaskOrderBO::getFinishQty)
                        )
                ));

        // ========== 3. 提取核心元数据（避免重复计算） ==========
        // 3.1 日期轴（X轴）：直接取TreeMap的Key，天然升序，无需手动排序
        List<String> xAxis = new ArrayList<>(dailyOpTaskMap.keySet());
        int dateCount = xAxis.size(); // 日期总数

        // 3.2 全局工序编码（去重+升序）+ 工序索引映射（消除indexOf性能损耗）
        TreeSet<String> allOpCodes = dailyOpTaskMap.values().stream()
                .flatMap(opTaskMap -> opTaskMap.keySet().stream())
                .collect(Collectors.toCollection(TreeSet::new));
        int opCount = allOpCodes.size(); // 工序总数（替代原max）

        // 预存「工序编码→索引」映射，O(1)查找，替代多次indexOf(O(n))
        Map<String, Integer> opCode2Index = new HashMap<>(opCount);
        int idx = 0;
        for (String opCode : allOpCodes) {
            opCode2Index.put(opCode, idx++);
        }

        // ========== 4. 初始化Y轴（按「日期维度」组织，每个日期对应所有工序的数量） ==========
        // Y轴结构：子列表数=日期数，每个子列表长度=工序数，初始值0L（无数据补0）
        List<List<Long>> yAxis = new ArrayList<>(dateCount);
        for (int i = 0; i < dateCount; i++) {
            // 初始化子列表：长度=工序数，所有元素默认0L，避免后续空值
            List<Long> dateQtyList = new ArrayList<>(Collections.nCopies(opCount, 0L));
            yAxis.add(dateQtyList);
        }

        // ========== 5. 填充Y轴数据（高性能，无冗余查找） ==========
        for (int dateIndex = 0; dateIndex < dateCount; dateIndex++) {
            String daily = xAxis.get(dateIndex); // 当前日期（直接按索引取，无indexOf）
            Map<String, Long> opTaskMap = dailyOpTaskMap.get(daily);
            if (CollectionUtils.isEmpty(opTaskMap)) {
                continue; // 当日无数据，保持0L
            }

            // 遍历当日工序，填充对应数量
            for (Map.Entry<String, Long> opEntry : opTaskMap.entrySet()) {
                String opCode = opEntry.getKey();
                // 防御性处理：避免工序编码不在全局列表中（理论上不会出现）
                Integer opIndex = opCode2Index.get(opCode);
                if (opIndex == null) {
                    continue;
                }
                // 空值补0，避免null（summingLong理论不会返回null，防御性处理）
                Long finishQty = Optional.ofNullable(opEntry.getValue()).orElse(0L);
                // 填充数据：日期索引 + 工序索引 定位值
                yAxis.get(dateIndex).set(opIndex, finishQty);
            }
        }

        // ========== 6. 封装返回VO ==========
        DailyOpTaskOrderVO dailyTaskOrderVO = new DailyOpTaskOrderVO();
        dailyTaskOrderVO.setXAxis(xAxis);
        dailyTaskOrderVO.setYAxis(yAxis);
        return dailyTaskOrderVO;
    }

    /**
     * 工具：短路遍历缓存，判断缓存是否存在指定taskOrderId，存在则返回实体，不存在返回null
     */
    private TaskOrder findTaskOrderInCache(String taskOrderId) {
        for (ConcurrentHashMap<String, ConcurrentHashMap<String, TaskOrder>> workLayer : taskOrderCache.values()) {
            for (ConcurrentHashMap<String, TaskOrder> taskLayer : workLayer.values()) {
                TaskOrder target = taskLayer.get(taskOrderId);
                if (target != null) {
                    return target;
                }
            }
        }
        return null;
    }

    /**
     * 加载派工单缓存（防并发穿透）
     *
     * @param taskOrderId 派工单ID
     */
    public void loadOrderCache(String taskOrderId) {
        // 先快速校验缓存是否已有，短路遍历，无全量扫描
        TaskOrder exist = findTaskOrderInCache(taskOrderId);
        if (exist != null) {
            return;
        }

        // 加锁：同一taskOrderId仅允许一个线程查库
        ReentrantLock lock = loadLockMap.computeIfAbsent(taskOrderId, k -> new ReentrantLock());
        lock.lock();
        try {
            // 双重检查：加锁后再次判断，防止其他线程已加载完成
            TaskOrder doubleCheck = findTaskOrderInCache(taskOrderId);
            if (doubleCheck != null) {
                return;
            }
            // 根据派工单ID查询，仅单条数据
            List<TaskOrder> taskOrderList = taskOrderMapper.selectTaskOrderListByTaskOrderId(taskOrderId);
            if (CollectionUtils.isEmpty(taskOrderList)) {
                return;
            }
            for (TaskOrder taskOrder : taskOrderList) {
                String orderId = taskOrder.getOrderId();
                String workOrderId = taskOrder.getWorkOrderId();
                // 原子创建分层Map，写入父缓存，不会丢失层级
                ConcurrentHashMap<String, ConcurrentHashMap<String, TaskOrder>> workMap =
                        taskOrderCache.computeIfAbsent(orderId, k -> new ConcurrentHashMap<>());
                ConcurrentHashMap<String, TaskOrder> taskMap =
                        workMap.computeIfAbsent(workOrderId, k -> new ConcurrentHashMap<>());
                // put覆盖旧数据，同步数据库最新状态
                taskMap.put(taskOrder.getId(), taskOrder);
            }
        } finally {
            lock.unlock();
            // 释放锁后删除，节约内存
            loadLockMap.remove(taskOrderId);
        }
    }

    /**
     * 根据taskOrderId获取派工单（无递归，线性流程）
     *
     * @param taskOrderId 派工单ID
     * @return 派工单，无数据返回null
     */
    private TaskOrder getTaskOrderCache(String taskOrderId) {
        // 1.先查缓存
        TaskOrder target = findTaskOrderInCache(taskOrderId);
        if (target != null) {
            return target;
        }
        // 2.缓存无数据，加载DB写入缓存
        loadOrderCache(taskOrderId);
        // 3.加载完成后再次查询一次，不递归
        return findTaskOrderInCache(taskOrderId);
    }

    /**
     * 更新/写入缓存（业务更新后调用，覆盖旧数据）
     *
     * @param orderId
     * @param workOrderId
     * @param taskOrderId
     * @param taskOrder
     */
    private void updateTaskOrderCache(String orderId, String workOrderId, String taskOrderId, TaskOrder taskOrder) {
        ConcurrentHashMap<String, ConcurrentHashMap<String, TaskOrder>> workMap =
                taskOrderCache.computeIfAbsent(orderId, k -> new ConcurrentHashMap<>());
        ConcurrentHashMap<String, TaskOrder> taskMap =
                workMap.computeIfAbsent(workOrderId, k -> new ConcurrentHashMap<>());
        taskMap.put(taskOrderId, taskOrder);
    }

    /**
     * 根据订单ID、工单ID移除对应整层派工单缓存，自动清理空父层级释放内存
     *
     * @param orderId 订单ID
     * @param workOrderId 工单ID
     */
    public void removeWorkOrderCache(String orderId, String workOrderId) {
        // 1. 获取订单对应的工单Map，不存在直接退出
        ConcurrentHashMap<String, ConcurrentHashMap<String, TaskOrder>> workMap = taskOrderCache.get(orderId);
        if (workMap == null) {
            return;
        }

        // 2. 删除当前工单下所有派工单缓存
        workMap.remove(workOrderId);

        // 3. 关键：如果该订单下所有工单都删空了，删除订单顶层节点，释放内存
        if (workMap.isEmpty()) {
            taskOrderCache.remove(orderId);
        }
    }

    /**
     * 仅移除指定订单-工单下单个派工单缓存，自动清理空层级释放内存
     *
     * @param orderId 订单ID
     * @param workOrderId 工单ID
     * @param taskOrderId 派工单ID
     */
    public void removeSingleTaskCache(String orderId, String workOrderId, String taskOrderId) {
        ConcurrentHashMap<String, ConcurrentHashMap<String, TaskOrder>> workMap = taskOrderCache.get(orderId);
        if (workMap == null) return;

        ConcurrentHashMap<String, TaskOrder> taskMap = workMap.get(workOrderId);
        if (taskMap == null) return;

        // 删除单个派工单
        taskMap.remove(taskOrderId);

        // 工单内派工单全部删空 → 删除工单节点
        if (taskMap.isEmpty()) {
            workMap.remove(workOrderId);
            // 订单下所有工单都删空 → 删除订单顶层节点
            if (workMap.isEmpty()) {
                taskOrderCache.remove(orderId);
            }
        }
    }

    /**
     * 根据orderId获取该订单下所有工单Map
     */
    private ConcurrentHashMap<String, ConcurrentHashMap<String, TaskOrder>> getWorkMapByOrderId(String orderId) {
        return taskOrderCache.get(orderId);
    }

    /**
     * Spring容器关闭、服务停机触发
     * ContextClosedEvent 的广播时机早于 Bean 完全销毁完成，此时单例工厂已经进入销毁流程，不允许再使用 / 创建业务层 Bean、Mapper。
     * 你的停机缓存持久化逻辑写在了业务 Service 内部，监听关闭事件时容器正在销毁该 Service，直接触发保护异常。
     */
//    @EventListener(ContextClosedEvent.class)
    @PreDestroy
    public void onShutdownFlushCache() {
        log.info("==== 服务准备停机，开始持久化操作记录缓存 ====");
        if (operateRecordCache.isEmpty()) {
            log.info("操作记录缓存为空，无需持久化");
            return;
        }

        // 收集全部缓存日志
        List<OperateRecord> allRecordList = new ArrayList<>();
        operateRecordCache.values().forEach(allRecordList::addAll);
        int total = allRecordList.size();
        log.info("缓存待入库记录总数：{}", total);

        if (total <= 0) {
            operateRecordCache.clear();
            return;
        }

        try {
            // 分片批量入库 50条一批
            List<List<OperateRecord>> partitions = Lists.partition(allRecordList, 50);
            for (List<OperateRecord> sub : partitions) {
                operateRecordMapper.insertBatch(sub);
            }
            log.info("停机持久化操作记录完成，共写入{}条", total);
            // 清空缓存
            operateRecordCache.clear();
        } catch (Exception e) {
            log.error("服务停机持久化操作记录缓存异常，存在日志丢失风险！", e);
            // 生产可扩展：写入本地磁盘文件兜底
        }
    }

}
