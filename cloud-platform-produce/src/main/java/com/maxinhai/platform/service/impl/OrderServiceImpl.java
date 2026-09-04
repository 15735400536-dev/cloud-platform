package com.maxinhai.platform.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.bo.OrderInfoBO;
import com.maxinhai.platform.bo.ProductBomAndRoutingBO;
import com.maxinhai.platform.dto.OrderAddDTO;
import com.maxinhai.platform.dto.OrderQueryDTO;
import com.maxinhai.platform.enums.OrderStatus;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.feign.MdmFeignClient;
import com.maxinhai.platform.feign.SystemFeignClient;
import com.maxinhai.platform.mapper.*;
import com.maxinhai.platform.po.Order;
import com.maxinhai.platform.po.Product;
import com.maxinhai.platform.po.TaskOrder;
import com.maxinhai.platform.po.WorkOrder;
import com.maxinhai.platform.po.technology.Bom;
import com.maxinhai.platform.po.technology.Routing;
import com.maxinhai.platform.po.technology.RoutingOperationRel;
import com.maxinhai.platform.service.OrderInfoService;
import com.maxinhai.platform.service.OrderService;
import com.maxinhai.platform.utils.AjaxResult;
import com.maxinhai.platform.utils.DateUtils;
import com.maxinhai.platform.vo.OrderProgressVO;
import com.maxinhai.platform.vo.OrderVO;
import com.maxinhai.platform.vo.technology.BomDetailVO;
import com.maxinhai.platform.vo.technology.BomInfoVO;
import com.maxinhai.platform.vo.technology.OperationVO;
import com.maxinhai.platform.vo.technology.RoutingInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
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
    @Resource
    private ProductMapper productMapper;
    @Resource
    private OrderInfoService orderInfoService;
    @Resource
    private SystemFeignClient systemFeign;
    @Resource
    private MdmFeignClient mdmFeign;

    @Override
    public Page<OrderVO> searchByPage(OrderQueryDTO param) {
        return orderMapper.selectJoinPage(param.getPage(), OrderVO.class,
                new MPJLambdaWrapper<Order>()
                        .innerJoin(Product.class, Product::getId, Order::getProductId)
                        .innerJoin(Bom.class, Bom::getId, Order::getBomId)
                        .innerJoin(Routing.class, Routing::getId, Order::getRoutingId)
                        // 查询条件
                        .eq(StrUtil.isNotBlank(param.getOrderCode()), Order::getOrderCode, param.getOrderCode())
                        .eq(Objects.nonNull(param.getOrderType()) && !Objects.equals(-1, param.getOrderType()), Order::getOrderType, param.getOrderType())
                        .eq(Objects.nonNull(param.getOrderStatus()) && !OrderStatus.ALL.equals(param.getOrderStatus()), Order::getOrderStatus, param.getOrderStatus())
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
        if (CollectionUtils.isEmpty(relList)) {
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

//    @PostConstruct
    public void initData() {
        List<ProductBomAndRoutingBO> productBomAndRoutingBOS = orderMapper.queryProductBomAndRouting();
        if (CollectionUtils.isEmpty(productBomAndRoutingBOS)) {
            return;
        }

        AjaxResult<List<String>> ajaxResult = systemFeign.generateCode("order", productBomAndRoutingBOS.size());
        if (ajaxResult.getCode() != HttpStatus.OK.value()) {
            log.error("生成订单编码出错: {}", ajaxResult.getMsg());
            return;
        }

        List<String> ordderCodeList = ajaxResult.getData();
        for (int i = 0; i < productBomAndRoutingBOS.size(); i++) {
            ProductBomAndRoutingBO productBomAndRoutingBO = productBomAndRoutingBOS.get(i);

            // 构建创建订单参数
            OrderAddDTO param = new OrderAddDTO();
            param.setOrderCode(ordderCodeList.get(i));
            param.setQty(RandomUtil.randomInt(10));
            param.setOrderType(1);
            param.setPlanBeginTime(new Date());
            param.setPlanEndTime(DateUtil.offsetDay(param.getPlanBeginTime(), 30));
            param.setProductId(productBomAndRoutingBO.getProductId());
            param.setBomId(productBomAndRoutingBO.getBomId());
            param.setRoutingId(productBomAndRoutingBO.getRoutingId());
            // 调用方法
            add(param);
        }
    }

    /**
     * 递归查找电机半成品BOM
     * @param productCode
     * @param bomVersion
     * @param bomInfoVOList
     */
    private void queryBomInfo(String productCode, String bomVersion, List<BomInfoVO> bomInfoVOList)  {
        // 查询BOM
        AjaxResult<BomInfoVO> bomInfoAjaxResult = mdmFeign.queryBomInfo(productCode, bomVersion);
        if (bomInfoAjaxResult.getCode() != HttpStatus.OK.value()) {
            log.error("查询产品【{}】版本号【{}】BOM失败: {}", productCode, bomVersion, bomInfoAjaxResult.getMsg());
            return;
        }
        BomInfoVO bomInfoVO = bomInfoAjaxResult.getData();
        bomInfoVOList.add(bomInfoVO);
        List<BomDetailVO> bomDetailList = bomInfoVO.getBomDetailList();
        for (BomDetailVO bomDetailVO : bomDetailList) {
            queryBomInfo(bomDetailVO.getMaterialCode(), bomVersion, bomInfoVOList);
        }
    }

    /**
     * 创建电机订单
     *
     * @param productCode
     * @param routingCode
     * @param routingVersion
     * @param bomCode
     * @param bomVersion
     * @param orderCount
     */
    public void createOrder(String productCode, Integer orderCount, Date planBeginTime, Date planEndTime,
                             String routingCode, String routingVersion,
                             String bomCode, String bomVersion) {

        // 查询电机BOM
        AjaxResult<BomInfoVO> bomInfoAjaxResult = mdmFeign.queryBomInfo(productCode, bomVersion);
        if (bomInfoAjaxResult.getCode() != HttpStatus.OK.value()) {
            log.error("查询产品【{}】版本号【{}】BOM失败: {}", productCode, bomVersion, bomInfoAjaxResult.getMsg());
            return;
        }

        // 查询电机工艺路线
        AjaxResult<RoutingInfoVO> routingInfoAjaxResult = mdmFeign.queryRoutingInfo(productCode, routingVersion);
        if (routingInfoAjaxResult.getCode() != HttpStatus.OK.value()) {
            log.error("查询产品【{}】版本号【{}】工艺路线明细失败: {}", productCode, routingVersion, routingInfoAjaxResult.getMsg());
            return;
        }
        BomInfoVO bomInfoVO = bomInfoAjaxResult.getData();
        RoutingInfoVO routingInfoVO = routingInfoAjaxResult.getData();

        // 生成订单编码
        AjaxResult<List<String>> codeRuleAjaxResult = systemFeign.generateCode("order", 1);
        if (codeRuleAjaxResult.getCode() != HttpStatus.OK.value()) {
            log.error("生成订单编号失败: {}", codeRuleAjaxResult.getMsg());
            return;
        }

        // 创建订单
        Order order = new Order();
        order.setOrderCode(codeRuleAjaxResult.getData().get(0));
        order.setQty(orderCount);
        order.setOrderType(1);
        order.setOrderTime(new Date());
        order.setOrderStatus(OrderStatus.INIT);
        order.setPlanBeginTime(planBeginTime);
        order.setPlanEndTime(planEndTime);
        order.setProductId(bomInfoVO.getProductId());
        order.setBomId(bomInfoVO.getId());
        order.setRoutingId(routingInfoVO.getId());
        orderMapper.insert(order);

        // 获取BOM明细
        List<BomDetailVO> bomDetailVOList = bomInfoVO.getBomDetailList();

        // 递归查找电机半成品BOM
        List<BomInfoVO> bomInfoVOList = new ArrayList<>();
        for (BomDetailVO bomDetailVO : bomDetailVOList) {
            String materialCode = bomDetailVO.getMaterialCode();
            queryBomInfo(materialCode, bomVersion, bomInfoVOList);
        }
        bomInfoVOList.add(bomInfoVO);

        // 查询产品信息
        List<String> productCodeList = bomInfoVOList.stream().map(BomInfoVO::getProductCode).collect(Collectors.toList());
        Map<String, String> productMap = productMapper.selectList(new LambdaQueryWrapper<Product>()
                        .select(Product::getId, Product::getCode, Product::getName)
                        .in(Product::getCode, productCodeList))
                .stream()
                .collect(Collectors.toMap(Product::getCode, Product::getId));

        Map<String, BomInfoVO> bomInfoMap = bomInfoVOList.stream().collect(Collectors.toMap(BomInfoVO::getProductCode, BomInfoVO->BomInfoVO));
        for (Map.Entry<String, BomInfoVO> entry : bomInfoMap.entrySet()) {
            String productCodeKey = entry.getKey();
            BomInfoVO bomInfoVC = entry.getValue();

            // 生成工单编码
            AjaxResult<List<String>> codeRuleAjaxResult1 = systemFeign.generateCode("workOrder", 1);
            if (codeRuleAjaxResult1.getCode() != HttpStatus.OK.value()) {
                log.error("生成工单编号失败: {}", codeRuleAjaxResult1.getMsg());
                continue;
            }

            // 查询产品工艺路线
            AjaxResult<RoutingInfoVO> routingDetailAjaxResult = mdmFeign.queryRoutingInfo(productCodeKey, routingVersion);
            if (routingDetailAjaxResult.getCode() != HttpStatus.OK.value()) {
                log.error("查询产品【{}】版本号【{}】工艺路线明细失败: {}", productCodeKey, routingVersion, routingDetailAjaxResult.getMsg());
                continue;
            }
            RoutingInfoVO routingInfoVO1 = routingDetailAjaxResult.getData();

            // 创建工单
            WorkOrder workOrder = new WorkOrder();
            workOrder.setWorkOrderCode(codeRuleAjaxResult1.getData().get(0));
            workOrder.setQty(1);
            workOrder.setOrderStatus(OrderStatus.INIT);
            workOrder.setPlanBeginTime(order.getPlanBeginTime());
            workOrder.setPlanEndTime(order.getPlanEndTime());
            workOrder.setProductId(productMap.get(productCodeKey));
            workOrder.setBomId(bomInfoVC.getId());
            workOrder.setRoutingId(routingInfoVO1.getId());
            workOrder.setOrderId(order.getId());
            workOrderMapper.insert(workOrder);

            // 创建派工单
            List<OperationVO> opVoList = routingInfoVO.getOperationList();
            for (int i = 0; i < opVoList.size(); i++) {
                Integer opSort = i + 1;
                OperationVO opVo = opVoList.get(i);
                TaskOrder taskOrder = new TaskOrder();
                taskOrder.setTaskOrderCode(workOrder.getWorkOrderCode() + "_" + opSort);
                taskOrder.setStatus(OrderStatus.INIT);
                taskOrder.setPlanBeginTime(order.getPlanBeginTime());
                taskOrder.setPlanEndTime(order.getPlanEndTime());
                taskOrder.setProductId(workOrder.getProductId());
                taskOrder.setBomId(workOrder.getBomId());
                taskOrder.setRoutingId(workOrder.getRoutingId());
                taskOrder.setOperationId(opVo.getId());
                taskOrder.setSort(opSort);
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

    @Override
    public OrderProgressVO orderProgress() {
        // 查询订单信息
        List<Order> orderList = orderMapper.selectList(new LambdaQueryWrapper<Order>()
                .select(Order::getId, Order::getQty, Order::getOrderStatus, Order::getActualEndTime));
        long orderFinishQty = orderList.stream().filter(order -> OrderStatus.REPORT.equals(order.getOrderStatus())).count();
        long orderUnFinishQty = orderList.stream().filter(order -> !OrderStatus.REPORT.equals(order.getOrderStatus())).count();
        long orderTodayFinishQty = orderList.stream().filter(order -> OrderStatus.REPORT.equals(order.getOrderStatus())
                && DateUtils.getBeginTimeOfToday().compareTo(order.getActualEndTime()) <= 0
                && DateUtils.getEndTimeOfToday().compareTo(order.getActualEndTime()) >= 0).count();
        Set<String> orderFinishDaySet = orderList.stream()
                .filter(order -> OrderStatus.REPORT.equals(order.getOrderStatus()))
                .map(order -> DateUtil.format(order.getActualEndTime(), "yyyy-MM-dd"))
                .collect(Collectors.toSet());

        // 查询工单信息
        List<WorkOrder> workOrderList = workOrderMapper.selectList(new LambdaQueryWrapper<WorkOrder>()
                .select(WorkOrder::getId, WorkOrder::getQty, WorkOrder::getOrderStatus, WorkOrder::getActualEndTime));
        long workOrderFinishQty = workOrderList.stream().filter(workOrder -> OrderStatus.REPORT.equals(workOrder.getOrderStatus())).count();
        long workOrderUnFinishQty = workOrderList.stream().filter(workOrder -> !OrderStatus.REPORT.equals(workOrder.getOrderStatus())).count();
        long workOrderTodayFinishQty = workOrderList.stream().filter(workOrder -> OrderStatus.REPORT.equals(workOrder.getOrderStatus())
                && DateUtils.getBeginTimeOfToday().compareTo(workOrder.getActualEndTime()) <= 0
                && DateUtils.getEndTimeOfToday().compareTo(workOrder.getActualEndTime()) >= 0).count();
        Set<String> workOrderFinishDaySet = workOrderList.stream()
                .filter(workOrder -> OrderStatus.REPORT.equals(workOrder.getOrderStatus()))
                .map(workOrder -> DateUtil.format(workOrder.getActualEndTime(), "yyyy-MM-dd"))
                .collect(Collectors.toSet());

        // 查询派工单信息
        List<TaskOrder> taskOrderList = taskOrderMapper.selectList(new LambdaQueryWrapper<TaskOrder>()
                .select(TaskOrder::getId, TaskOrder::getStatus, TaskOrder::getActualEndTime));
        long taskOrderFinishQty = taskOrderList.stream().filter(taskOrder -> OrderStatus.REPORT.equals(taskOrder.getStatus())).count();
        long taskOrderUnFinishQty = taskOrderList.stream().filter(taskOrder -> !OrderStatus.REPORT.equals(taskOrder.getStatus())).count();
        long taskOrderTodayFinishQty = taskOrderList.stream().filter(taskOrder -> OrderStatus.REPORT.equals(taskOrder.getStatus())
                && DateUtils.getBeginTimeOfToday().compareTo(taskOrder.getActualEndTime()) <= 0
                && DateUtils.getEndTimeOfToday().compareTo(taskOrder.getActualEndTime()) >= 0).count();
        Set<String> taskOrderFinishDaySet = taskOrderList.stream()
                .filter(taskOrder -> OrderStatus.REPORT.equals(taskOrder.getStatus()))
                .map(taskOrder -> DateUtil.format(taskOrder.getActualEndTime(), "yyyy-MM-dd"))
                .collect(Collectors.toSet());

        OrderProgressVO resultVO = new OrderProgressVO();
        resultVO.setOrderTotalQty(orderList.size());
        resultVO.setOrderFinishQty(orderFinishQty);
        resultVO.setOrderUnFinishQty(orderUnFinishQty);
        resultVO.setOrderProgress(orderList.isEmpty() ? "0.00%" : NumberUtil.formatPercent(NumberUtil.div(orderFinishQty, orderList.size(), 4), 2));

        resultVO.setWorkOrderTotalQty(workOrderList.size());
        resultVO.setWorkOrderFinishQty(workOrderFinishQty);
        resultVO.setWorkOrderUnFinishQty(workOrderUnFinishQty);
        resultVO.setWorkOrderProgress(workOrderList.isEmpty() ? "0.00%" : NumberUtil.formatPercent(NumberUtil.div(workOrderFinishQty, workOrderList.size(), 4), 2));

        resultVO.setTaskOrderTotalQty(taskOrderList.size());
        resultVO.setTaskOrderFinishQty(taskOrderFinishQty);
        resultVO.setTaskOrderUnFinishQty(taskOrderUnFinishQty);
        resultVO.setTaskOrderProgress(taskOrderList.isEmpty() ? "0.00%" : NumberUtil.formatPercent(NumberUtil.div(taskOrderFinishQty, taskOrderList.size(), 4), 2));

        resultVO.setOrderTodayFinishQty(orderTodayFinishQty);
        resultVO.setWorkOrderTodayFinishQty(workOrderTodayFinishQty);
        resultVO.setTaskOrderTodayFinishQty(taskOrderTodayFinishQty);

        resultVO.setOrderAvgFinishQty(orderFinishQty == 0 || orderFinishDaySet.isEmpty() ? 0 : NumberUtil.div(orderFinishQty, orderFinishDaySet.size(), 2, RoundingMode.HALF_UP));
        resultVO.setWorkOrderAvgFinishQty(workOrderFinishQty == 0 || workOrderFinishDaySet.isEmpty() ? 0 : NumberUtil.div(workOrderFinishQty, workOrderFinishDaySet.size(), 2, RoundingMode.HALF_UP));
        resultVO.setTaskOrderAvgFinishQty(taskOrderFinishQty == 0 || taskOrderFinishDaySet.isEmpty() ? 0 : NumberUtil.div(taskOrderFinishQty, taskOrderFinishDaySet.size(), 2, RoundingMode.HALF_UP));

        resultVO.setExpectedFinishDays(NumberUtil.div(BigDecimal.valueOf(taskOrderUnFinishQty), BigDecimal.valueOf(resultVO.getTaskOrderAvgFinishQty()), 0, RoundingMode.HALF_UP).intValue());
        resultVO.setExpectedRealTimeFinishDays(NumberUtil.div(BigDecimal.valueOf(taskOrderUnFinishQty), BigDecimal.valueOf(taskOrderTodayFinishQty), 0, RoundingMode.HALF_UP).intValue());
        resultVO.setActualFinishDays(taskOrderFinishDaySet.size());
        return resultVO;
    }

    @Override
    public OrderProgressVO orderProgressEx() {
        // 问题 1：1 个接口 = 15 次 SQL 查询（性能灾难）
        // 问题 2：无任何缓存，每次都查库
        // 问题 3：统计全表数据，非常消耗数据库与内存
        // 问题 4：被高频率调用，并发爆炸
//        OrderProgressVO resultVO = new OrderProgressVO();
//        resultVO.setOrderTotalQty(orderMapper.countOrderTotalQty());
//        resultVO.setOrderFinishQty(orderMapper.countOrderFinishQty());
//        resultVO.setOrderUnFinishQty(orderMapper.countOrderUnFinishQty());
//        resultVO.setOrderProgress(resultVO.getOrderTotalQty() == 0 ? "0.00%" : NumberUtil.formatPercent(NumberUtil.div(resultVO.getOrderFinishQty(), resultVO.getOrderTotalQty(), 4), 2));
//
//        resultVO.setWorkOrderTotalQty(orderMapper.countWorkOrderTotalQty());
//        resultVO.setWorkOrderFinishQty(orderMapper.countWorkOrderFinishQty());
//        resultVO.setWorkOrderUnFinishQty(orderMapper.countWorkOrderUnFinishQty());
//        resultVO.setWorkOrderProgress(resultVO.getWorkOrderTotalQty() == 0 ? "0.00%" : NumberUtil.formatPercent(NumberUtil.div(resultVO.getWorkOrderFinishQty(), resultVO.getWorkOrderTotalQty(), 4), 2));
//
//        resultVO.setTaskOrderTotalQty(orderMapper.countTaskOrderTotalQty());
//        resultVO.setTaskOrderFinishQty(orderMapper.countTaskOrderFinishQty());
//        resultVO.setTaskOrderUnFinishQty(orderMapper.countTaskOrderUnFinishQty());
//        resultVO.setTaskOrderProgress(resultVO.getTaskOrderTotalQty() == 0 ? "0.00%" : NumberUtil.formatPercent(NumberUtil.div(resultVO.getTaskOrderFinishQty(), resultVO.getTaskOrderTotalQty(), 4), 2));
//
//        resultVO.setOrderTodayFinishQty(orderMapper.countOrderTodayFinishQty());
//        resultVO.setWorkOrderTodayFinishQty(orderMapper.countWorkOrderTodayFinishQty());
//        resultVO.setTaskOrderTodayFinishQty(orderMapper.countTaskOrderTodayFinishQty());
//
//        long orderFinishDays = orderMapper.countOrderFinishDays();
//        long workOrderFinishDays = orderMapper.countWorkOrderFinishDays();
//        long taskOrderFinishDays = orderMapper.countTaskOrderFinishDays();
//        resultVO.setOrderAvgFinishQty(resultVO.getOrderFinishQty() == 0 || orderFinishDays == 0 ? 0 : NumberUtil.div(resultVO.getOrderFinishQty(), orderFinishDays, 2, RoundingMode.HALF_UP));
//        resultVO.setWorkOrderAvgFinishQty(resultVO.getWorkOrderFinishQty() == 0 || workOrderFinishDays == 0 ? 0 : NumberUtil.div(resultVO.getWorkOrderFinishQty(), workOrderFinishDays, 2, RoundingMode.HALF_UP));
//        resultVO.setTaskOrderAvgFinishQty(resultVO.getTaskOrderFinishQty() == 0 || taskOrderFinishDays == 0 ? 0 : NumberUtil.div(resultVO.getTaskOrderFinishQty(), taskOrderFinishDays, 2, RoundingMode.HALF_UP));
//
//        resultVO.setExpectedFinishDays(NumberUtil.div(BigDecimal.valueOf(resultVO.getTaskOrderUnFinishQty()), BigDecimal.valueOf(resultVO.getTaskOrderAvgFinishQty()), 0, RoundingMode.HALF_UP).intValue());
//        resultVO.setExpectedRealTimeFinishDays(NumberUtil.div(BigDecimal.valueOf(resultVO.getTaskOrderUnFinishQty()), BigDecimal.valueOf(resultVO.getTaskOrderTodayFinishQty()), 0, RoundingMode.HALF_UP).intValue());
//        resultVO.setActualFinishDays(orderMapper.countTaskOrderFinishDays());
//        return resultVO;

        // 1次SQL查询所有统计数据
        OrderProgressVO resultVO = orderMapper.countAllProgressData();

        // 计算进度
        resultVO.setOrderProgress(calcProgress(resultVO.getOrderFinishQty(), resultVO.getOrderTotalQty()));
        resultVO.setWorkOrderProgress(calcProgress(resultVO.getWorkOrderFinishQty(), resultVO.getWorkOrderTotalQty()));
        resultVO.setTaskOrderProgress(calcProgress(resultVO.getTaskOrderFinishQty(), resultVO.getTaskOrderTotalQty()));

        // 计算平均值
        long orderFinishDays = resultVO.getOrderFinishDays();
        long workFinishDays = resultVO.getWorkOrderFinishDays();
        long taskFinishDays = resultVO.getTaskOrderFinishDays();

        resultVO.setOrderAvgFinishQty(calcAvg(resultVO.getOrderFinishQty(), orderFinishDays));
        resultVO.setWorkOrderAvgFinishQty(calcAvg(resultVO.getWorkOrderFinishQty(), workFinishDays));
        resultVO.setTaskOrderAvgFinishQty(calcAvg(resultVO.getTaskOrderFinishQty(), taskFinishDays));

        // 计算预计天数
        resultVO.setExpectedFinishDays(calcDays(resultVO.getTaskOrderUnFinishQty(), resultVO.getTaskOrderAvgFinishQty()));
        resultVO.setExpectedRealTimeFinishDays(calcDays(resultVO.getTaskOrderUnFinishQty(), resultVO.getTaskOrderTodayFinishQty()));
        resultVO.setActualFinishDays((int) taskFinishDays);

        return resultVO;
    }

    // 工具方法：计算进度百分比
    private String calcProgress(long finish, long total) {
        if (total == 0) return "0.00%";
        return NumberUtil.formatPercent(NumberUtil.div(finish, total, 4), 2);
    }

    // 工具方法：计算平均值
    private double calcAvg(long qty, long days) {
        if (qty == 0 || days == 0) return 0;
        return NumberUtil.div(qty, days, 2, RoundingMode.HALF_UP);
    }

    // 工具方法：计算天数
    private int calcDays(long unFinish, double avg) {
        if (avg <= 0) return 0;
        return NumberUtil.div(BigDecimal.valueOf(unFinish), BigDecimal.valueOf(avg), 0, RoundingMode.HALF_UP).intValue();
    }

    @Override
    public OrderProgressVO orderProgressEx1() {
        OrderInfoBO orderInfo = orderMapper.countOrderInfo();
        OrderProgressVO resultVO = new OrderProgressVO();
        resultVO.setOrderTotalQty(orderInfo.getTotalQty());
        resultVO.setOrderFinishQty(orderInfo.getFinishQty());
        resultVO.setOrderUnFinishQty(orderInfo.getUnfinishQty());
        resultVO.setOrderProgress(resultVO.getOrderTotalQty() == 0 ? "0.00%" : NumberUtil.formatPercent(NumberUtil.div(resultVO.getOrderFinishQty(), resultVO.getOrderTotalQty(), 4), 2));

        OrderInfoBO workOrderInfo = orderMapper.countWorkOrderInfo();
        resultVO.setWorkOrderTotalQty(workOrderInfo.getTotalQty());
        resultVO.setWorkOrderFinishQty(workOrderInfo.getFinishQty());
        resultVO.setWorkOrderUnFinishQty(workOrderInfo.getUnfinishQty());
        resultVO.setWorkOrderProgress(resultVO.getWorkOrderTotalQty() == 0 ? "0.00%" : NumberUtil.formatPercent(NumberUtil.div(resultVO.getWorkOrderFinishQty(), resultVO.getWorkOrderTotalQty(), 4), 2));

        OrderInfoBO taskOrderInfo = orderMapper.countTaskOrderInfo();
        resultVO.setTaskOrderTotalQty(taskOrderInfo.getTotalQty());
        resultVO.setTaskOrderFinishQty(taskOrderInfo.getFinishQty());
        resultVO.setTaskOrderUnFinishQty(taskOrderInfo.getUnfinishQty());
        resultVO.setTaskOrderProgress(resultVO.getTaskOrderTotalQty() == 0 ? "0.00%" : NumberUtil.formatPercent(NumberUtil.div(resultVO.getTaskOrderFinishQty(), resultVO.getTaskOrderTotalQty(), 4), 2));

        resultVO.setOrderTodayFinishQty(orderInfo.getTodayFinishQty());
        resultVO.setWorkOrderTodayFinishQty(workOrderInfo.getTodayFinishQty());
        resultVO.setTaskOrderTodayFinishQty(taskOrderInfo.getTodayFinishQty());

        long orderFinishDays = orderInfo.getFinishDays();
        long workOrderFinishDays = workOrderInfo.getFinishDays();
        long taskOrderFinishDays = taskOrderInfo.getFinishDays();
        resultVO.setOrderAvgFinishQty(resultVO.getOrderFinishQty() == 0 || orderFinishDays == 0 ? 0 : NumberUtil.div(resultVO.getOrderFinishQty(), orderFinishDays, 2, RoundingMode.HALF_UP));
        resultVO.setWorkOrderAvgFinishQty(resultVO.getWorkOrderFinishQty() == 0 || workOrderFinishDays == 0 ? 0 : NumberUtil.div(resultVO.getWorkOrderFinishQty(), workOrderFinishDays, 2, RoundingMode.HALF_UP));
        resultVO.setTaskOrderAvgFinishQty(resultVO.getTaskOrderFinishQty() == 0 || taskOrderFinishDays == 0 ? 0 : NumberUtil.div(resultVO.getTaskOrderFinishQty(), taskOrderFinishDays, 2, RoundingMode.HALF_UP));

        resultVO.setExpectedFinishDays(NumberUtil.div(BigDecimal.valueOf(resultVO.getTaskOrderUnFinishQty()), BigDecimal.valueOf(resultVO.getTaskOrderAvgFinishQty()), 0, RoundingMode.HALF_UP).intValue());
        resultVO.setExpectedRealTimeFinishDays(NumberUtil.div(BigDecimal.valueOf(resultVO.getTaskOrderUnFinishQty()), BigDecimal.valueOf(resultVO.getTaskOrderTodayFinishQty()), 0, RoundingMode.HALF_UP).intValue());
        resultVO.setActualFinishDays(taskOrderFinishDays);
        return resultVO;
    }

    @Override
    public OrderProgressVO orderProgressEx2() {
        OrderInfoBO orderInfo = orderMapper.countOrderSimpleInfo();
        OrderProgressVO resultVO = new OrderProgressVO();
        resultVO.setOrderTotalQty(orderInfo.getTotalQty());
        resultVO.setOrderFinishQty(orderInfo.getFinishQty());
        resultVO.setOrderUnFinishQty(orderInfo.getUnfinishQty());
        resultVO.setOrderProgress(resultVO.getOrderTotalQty() == 0 ? "0.00%" : NumberUtil.formatPercent(NumberUtil.div(resultVO.getOrderFinishQty(), resultVO.getOrderTotalQty(), 4), 2));

        OrderInfoBO workOrderInfo = orderMapper.countWorkOrderSimpleInfo();
        resultVO.setWorkOrderTotalQty(workOrderInfo.getTotalQty());
        resultVO.setWorkOrderFinishQty(workOrderInfo.getFinishQty());
        resultVO.setWorkOrderUnFinishQty(workOrderInfo.getUnfinishQty());
        resultVO.setWorkOrderProgress(resultVO.getWorkOrderTotalQty() == 0 ? "0.00%" : NumberUtil.formatPercent(NumberUtil.div(resultVO.getWorkOrderFinishQty(), resultVO.getWorkOrderTotalQty(), 4), 2));

        OrderInfoBO taskOrderInfo = orderMapper.countTaskOrderSimpleInfo();
        resultVO.setTaskOrderTotalQty(taskOrderInfo.getTotalQty());
        resultVO.setTaskOrderFinishQty(taskOrderInfo.getFinishQty());
        resultVO.setTaskOrderUnFinishQty(taskOrderInfo.getUnfinishQty());
        resultVO.setTaskOrderProgress(resultVO.getTaskOrderTotalQty() == 0 ? "0.00%" : NumberUtil.formatPercent(NumberUtil.div(resultVO.getTaskOrderFinishQty(), resultVO.getTaskOrderTotalQty(), 4), 2));

        resultVO.setOrderTodayFinishQty(orderInfo.getTodayFinishQty());
        resultVO.setWorkOrderTodayFinishQty(workOrderInfo.getTodayFinishQty());
        resultVO.setTaskOrderTodayFinishQty(taskOrderInfo.getTodayFinishQty());

        long orderFinishDays = orderInfo.getFinishDays();
        long workOrderFinishDays = workOrderInfo.getFinishDays();
        long taskOrderFinishDays = taskOrderInfo.getFinishDays();
        resultVO.setOrderAvgFinishQty(resultVO.getOrderFinishQty() == 0 || orderFinishDays == 0 ? 0 : NumberUtil.div(resultVO.getOrderFinishQty(), orderFinishDays, 2, RoundingMode.HALF_UP));
        resultVO.setWorkOrderAvgFinishQty(resultVO.getWorkOrderFinishQty() == 0 || workOrderFinishDays == 0 ? 0 : NumberUtil.div(resultVO.getWorkOrderFinishQty(), workOrderFinishDays, 2, RoundingMode.HALF_UP));
        resultVO.setTaskOrderAvgFinishQty(resultVO.getTaskOrderFinishQty() == 0 || taskOrderFinishDays == 0 ? 0 : NumberUtil.div(resultVO.getTaskOrderFinishQty(), taskOrderFinishDays, 2, RoundingMode.HALF_UP));

        resultVO.setExpectedFinishDays(NumberUtil.div(BigDecimal.valueOf(resultVO.getTaskOrderUnFinishQty()), BigDecimal.valueOf(resultVO.getTaskOrderAvgFinishQty()), 0, RoundingMode.HALF_UP).intValue());
        resultVO.setExpectedRealTimeFinishDays(NumberUtil.div(BigDecimal.valueOf(resultVO.getTaskOrderUnFinishQty()), BigDecimal.valueOf(resultVO.getTaskOrderTodayFinishQty()), 0, RoundingMode.HALF_UP).intValue());
        resultVO.setActualFinishDays(taskOrderFinishDays);
        return resultVO;
    }

    @Override
    public OrderProgressVO orderProgressEx3() throws ExecutionException, InterruptedException {
        // 异步发起三个查询
        CompletableFuture<OrderInfoBO> orderFuture = orderInfoService.countOrderSimpleInfoAsync();
        CompletableFuture<OrderInfoBO> workOrderFuture = orderInfoService.countWorkOrderSimpleInfoAsync();
        CompletableFuture<OrderInfoBO> taskOrderFuture = orderInfoService.countTaskOrderSimpleInfoAsync();

        // 等待所有异步任务完成
        CompletableFuture.allOf(orderFuture, workOrderFuture, taskOrderFuture).join();

        OrderInfoBO orderInfo = orderFuture.get();
        OrderProgressVO resultVO = new OrderProgressVO();
        resultVO.setOrderTotalQty(orderInfo.getTotalQty());
        resultVO.setOrderFinishQty(orderInfo.getFinishQty());
        resultVO.setOrderUnFinishQty(orderInfo.getUnfinishQty());
        resultVO.setOrderProgress(resultVO.getOrderTotalQty() == 0 ? "0.00%" : NumberUtil.formatPercent(NumberUtil.div(resultVO.getOrderFinishQty(), resultVO.getOrderTotalQty(), 4), 2));

        OrderInfoBO workOrderInfo = workOrderFuture.get();
        resultVO.setWorkOrderTotalQty(workOrderInfo.getTotalQty());
        resultVO.setWorkOrderFinishQty(workOrderInfo.getFinishQty());
        resultVO.setWorkOrderUnFinishQty(workOrderInfo.getUnfinishQty());
        resultVO.setWorkOrderProgress(resultVO.getWorkOrderTotalQty() == 0 ? "0.00%" : NumberUtil.formatPercent(NumberUtil.div(resultVO.getWorkOrderFinishQty(), resultVO.getWorkOrderTotalQty(), 4), 2));

        OrderInfoBO taskOrderInfo = taskOrderFuture.get();
        resultVO.setTaskOrderTotalQty(taskOrderInfo.getTotalQty());
        resultVO.setTaskOrderFinishQty(taskOrderInfo.getFinishQty());
        resultVO.setTaskOrderUnFinishQty(taskOrderInfo.getUnfinishQty());
        resultVO.setTaskOrderProgress(resultVO.getTaskOrderTotalQty() == 0 ? "0.00%" : NumberUtil.formatPercent(NumberUtil.div(resultVO.getTaskOrderFinishQty(), resultVO.getTaskOrderTotalQty(), 4), 2));

        resultVO.setOrderTodayFinishQty(orderInfo.getTodayFinishQty());
        resultVO.setWorkOrderTodayFinishQty(workOrderInfo.getTodayFinishQty());
        resultVO.setTaskOrderTodayFinishQty(taskOrderInfo.getTodayFinishQty());

        long orderFinishDays = orderInfo.getFinishDays();
        long workOrderFinishDays = workOrderInfo.getFinishDays();
        long taskOrderFinishDays = taskOrderInfo.getFinishDays();
        resultVO.setOrderAvgFinishQty(resultVO.getOrderFinishQty() == 0 || orderFinishDays == 0 ? 0 : NumberUtil.div(resultVO.getOrderFinishQty(), orderFinishDays, 2, RoundingMode.HALF_UP));
        resultVO.setWorkOrderAvgFinishQty(resultVO.getWorkOrderFinishQty() == 0 || workOrderFinishDays == 0 ? 0 : NumberUtil.div(resultVO.getWorkOrderFinishQty(), workOrderFinishDays, 2, RoundingMode.HALF_UP));
        resultVO.setTaskOrderAvgFinishQty(resultVO.getTaskOrderFinishQty() == 0 || taskOrderFinishDays == 0 ? 0 : NumberUtil.div(resultVO.getTaskOrderFinishQty(), taskOrderFinishDays, 2, RoundingMode.HALF_UP));

        resultVO.setExpectedFinishDays(NumberUtil.div(BigDecimal.valueOf(resultVO.getTaskOrderUnFinishQty()), BigDecimal.valueOf(resultVO.getTaskOrderAvgFinishQty()), 0, RoundingMode.HALF_UP).intValue());
        resultVO.setExpectedRealTimeFinishDays(NumberUtil.div(BigDecimal.valueOf(resultVO.getTaskOrderUnFinishQty()), BigDecimal.valueOf(resultVO.getTaskOrderTodayFinishQty()), 0, RoundingMode.HALF_UP).intValue());
        resultVO.setActualFinishDays(taskOrderFinishDays);
        return resultVO;
    }
}
