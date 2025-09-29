package com.maxinhai.platform.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.maxinhai.platform.bo.CheckTemplateItemBO;
import com.maxinhai.platform.dto.CheckOrderAddDTO;
import com.maxinhai.platform.dto.CheckOrderEditDTO;
import com.maxinhai.platform.dto.CheckOrderQueryDTO;
import com.maxinhai.platform.enums.CheckStatus;
import com.maxinhai.platform.enums.CheckType;
import com.maxinhai.platform.exception.BusinessException;
import com.maxinhai.platform.mapper.*;
import com.maxinhai.platform.po.*;
import com.maxinhai.platform.po.technology.Operation;
import com.maxinhai.platform.service.CheckOrderDetailService;
import com.maxinhai.platform.service.CheckOrderService;
import com.maxinhai.platform.vo.CheckOrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CheckOrderServiceImpl extends ServiceImpl<CheckOrderMapper, CheckOrder> implements CheckOrderService {

    @Resource
    private CheckOrderMapper checkOrderMapper;
    @Resource
    private OrderMapper orderMapper;
    @Resource
    private WorkOrderMapper workOrderMapper;
    @Resource
    private TaskOrderMapper taskOrderMapper;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private OperationMapper operationMapper;
    @Resource
    private CheckTemplateMapper templateMapper;
    @Resource
    private CheckItemMapper itemMapper;
    @Resource
    private CheckTemplateItemRelMapper templateItemRelMapper;
    @Resource
    private CheckOrderDetailService checkOrderDetailService;

    @Override
    public Page<CheckOrderVO> searchByPage(CheckOrderQueryDTO param) {
        return checkOrderMapper.selectJoinPage(param.getPage(), CheckOrderVO.class,
                new MPJLambdaWrapper<CheckOrder>()
                        .innerJoin(Product.class, Product::getId, CheckOrder::getProductId)
                        // 查询条件
                        .like(StrUtil.isNotBlank(param.getOrderCode()), CheckOrder::getOrderCode, param.getOrderCode())
                        // 字段别名
                        .selectAll(CheckOrder.class)
                        .selectAs(Product::getCode, CheckOrderVO::getProductCode)
                        .selectAs(Product::getName, CheckOrderVO::getProductName)
                        // 排序
                        .orderByDesc(CheckOrder::getCreateTime));
    }

    @Override
    public CheckOrderVO getInfo(String id) {
        return checkOrderMapper.selectJoinOne(CheckOrderVO.class,
                new MPJLambdaWrapper<CheckOrder>()
                        .innerJoin(Product.class, Product::getId, CheckOrder::getProductId)
                        // 查询条件
                        .like(StrUtil.isNotBlank(id), CheckOrder::getId, id)
                        // 字段别名
                        .selectAll(CheckOrder.class)
                        .selectAs(Product::getCode, CheckOrderVO::getProductCode)
                        .selectAs(Product::getName, CheckOrderVO::getProductName));
    }

    @Override
    public void remove(String[] ids) {
        checkOrderMapper.deleteBatchIds(Arrays.stream(ids).collect(Collectors.toList()));
    }

    @Override
    public void edit(CheckOrderEditDTO param) {
        CheckOrder user = BeanUtil.toBean(param, CheckOrder.class);
        checkOrderMapper.updateById(user);
    }

    @Override
    public void add(CheckOrderAddDTO param) {
        CheckOrder user = BeanUtil.toBean(param, CheckOrder.class);
        checkOrderMapper.insert(user);
    }

    @Override
    public void generate(String workOrderId) {
        WorkOrder workOrder = workOrderMapper.selectById(workOrderId);
        if (Objects.isNull(workOrder)) {
            return;
        }
        log.info("生成订单质检单开始, workOrderId:{}", workOrderId);

        // 查询检测模板数据
        List<CheckTemplateItemBO> templateItemBOList = templateMapper.selectJoinList(CheckTemplateItemBO.class, new MPJLambdaWrapper<CheckTemplate>()
                .innerJoin(CheckTemplateItemRel.class, CheckTemplateItemRel::getTemplateId, CheckTemplate::getId)
                .innerJoin(CheckItem.class, CheckItem::getId, CheckTemplateItemRel::getItemId)
                // 字段别名
                .selectAll(CheckTemplateItemRel.class)
                .selectAs(CheckItem::getItemCode, CheckTemplateItemBO::getItemCode)
                .selectAs(CheckItem::getItemName, CheckTemplateItemBO::getItemName)
                .selectAs(CheckItem::getControlType, CheckTemplateItemBO::getControlType)
                .selectAs(CheckItem::getMaxValue, CheckTemplateItemBO::getMaxValue)
                .selectAs(CheckItem::getMinValue, CheckTemplateItemBO::getMinValue)
                // 查询条件
                .eq(CheckTemplate::getId, "1955268740873261057"));
        if (CollectionUtils.isEmpty(templateItemBOList)) {
            return;
        }

        // 创建检测单
        CheckOrder checkOrder = new CheckOrder();
        checkOrder.setOrderCode(workOrder.getWorkOrderCode());
        checkOrder.setCheckTemplateId("1955268740873261057");
        checkOrder.setCheckType(CheckType.SELF_CHECK);
        checkOrder.setProductId(workOrder.getProductId());
        checkOrder.setStatus(CheckStatus.NO);
        checkOrderMapper.insert(checkOrder);
        // 创建检测单明细
        List<CheckOrderDetail> orderDetailList = templateItemBOList.stream().map(bo -> {
            CheckOrderDetail checkOrderDetail = new CheckOrderDetail();
            checkOrderDetail.setCheckOrderId(checkOrder.getId());
            checkOrderDetail.setCheckItemId(bo.getItemId());
            checkOrderDetail.setItemCode(bo.getItemCode());
            checkOrderDetail.setItemName(bo.getItemName());
            checkOrderDetail.setControlType(bo.getControlType());
            checkOrderDetail.setMinValue(bo.getMinValue());
            checkOrderDetail.setMaxValue(bo.getMaxValue());
            checkOrderDetail.setStatus(CheckStatus.NO);
            return checkOrderDetail;
        }).collect(Collectors.toList());
        checkOrderDetailService.saveBatch(orderDetailList);
        log.info("生成订单质检单完成, workOrderId:{}", workOrderId);
    }

    @Override
    public void generateCheckOrder(String taskOrderId) {
        // 查询派工单信息
        TaskOrder taskOrder = taskOrderMapper.selectOne(new LambdaQueryWrapper<TaskOrder>()
                .select(TaskOrder::getId, TaskOrder::getTaskOrderCode, TaskOrder::getProductId, TaskOrder::getOperationId)
                .eq(TaskOrder::getId, taskOrderId));
        if (Objects.isNull(taskOrder)) {
            log.error("taskOrderId:{}，派工单不存在！", taskOrderId);
            return;
        }
        // 查询产品信息
        Product product = productMapper.selectOne(new LambdaQueryWrapper<Product>()
                .select(Product::getId, Product::getCode, Product::getName)
                .eq(Product::getId, taskOrder.getProductId()));
        if (Objects.isNull(product)) {
            log.error("productId:{}，产品不存在！", taskOrder.getProductId());
            return;
        }
        // 查询工序信息
        Operation operation = operationMapper.selectOne(new LambdaQueryWrapper<Operation>()
                .select(Operation::getId, Operation::getCode, Operation::getName)
                .eq(Operation::getId, taskOrder.getOperationId()));
        if (Objects.isNull(operation)) {
            log.error("operationId:{}，工序不存在！", taskOrder.getOperationId());
            return;
        }
        // 根据质检模板配置生成质检单
        generateSelfCheckOrder(taskOrder, product, operation);
        generateMutualCheckOrder(taskOrder, product, operation);
        generateSpecialCheckOrder(taskOrder, product, operation);
    }

    @Override
    public void generateSelfCheckOrder(String workOrderId, String operationId) {
        // 查询工单信息
        WorkOrder workOrder = workOrderMapper.selectOne(new LambdaQueryWrapper<WorkOrder>()
                .select(WorkOrder::getId, WorkOrder::getProductId, WorkOrder::getWorkOrderCode)
                .eq(WorkOrder::getId, workOrderId));
        // 查询产品信息
        Product product = productMapper.selectOne(new LambdaQueryWrapper<Product>()
                .select(Product::getId, Product::getCode, Product::getName)
                .eq(Product::getId, workOrder.getProductId()));
        // 查询工序信息
        Operation operation = operationMapper.selectOne(new LambdaQueryWrapper<Operation>()
                .select(Operation::getId, Operation::getCode, Operation::getName)
                .eq(Operation::getId, operationId));
        // 根据产品ID、工序ID查询自检模板
        List<CheckTemplate> checkTemplateList = templateMapper.selectList(new LambdaQueryWrapper<CheckTemplate>()
                .eq(CheckTemplate::getProductId, workOrder.getProductId())
                .eq(CheckTemplate::getOperationId, workOrder.getWorkOrderCode())
                .eq(CheckTemplate::getCheckType, CheckType.SELF_CHECK));
        if (CollectionUtils.isEmpty(checkTemplateList)) {
            return;
        }
        if (checkTemplateList.size() > 1) {
            throw new BusinessException("产品【" + product.getCode() + "】工序【" + operation.getCode() + "】自检模板数据异常！");
        }
        // 自检模板
        CheckTemplate template = checkTemplateList.get(0);
        // 自检模板明细
        List<CheckTemplateItemRel> relList = templateItemRelMapper.selectList(new LambdaQueryWrapper<CheckTemplateItemRel>()
                .select(CheckTemplateItemRel::getTemplateId, CheckTemplateItemRel::getItemId)
                .eq(CheckTemplateItemRel::getTemplateId, template.getId()));
        List<String> itemIds = relList.stream().map(CheckTemplateItemRel::getItemId).collect(Collectors.toList());
        // 查询自检项
        List<CheckItem> itemList = itemMapper.selectList(new LambdaQueryWrapper<CheckItem>()
                .select(CheckItem::getId, CheckItem::getItemCode, CheckItem::getItemName,
                        CheckItem::getControlType, CheckItem::getMaxValue, CheckItem::getMinValue)
                .eq(CheckItem::getId, itemIds));
        log.info("生成自检质检单开始, workOrderId:{}", workOrderId);
        // 创建检测单
        CheckOrder checkOrder = CheckOrder.buildSelfCheckOrder(product, workOrder, template);
        checkOrderMapper.insert(checkOrder);
        // 创建检测单明细
        List<CheckOrderDetail> orderDetailList = itemList.stream()
                .map(item -> CheckOrderDetail.build(checkOrder, item))
                .collect(Collectors.toList());
        checkOrderDetailService.saveBatch(orderDetailList);
        log.info("生成自检质检单结束, workOrderId:{}", workOrderId);
    }

    @Override
    public void generateMutualCheckOrder(String workOrderId, String operationId) {
        // 查询工单信息
        WorkOrder workOrder = workOrderMapper.selectOne(new LambdaQueryWrapper<WorkOrder>()
                .select(WorkOrder::getId, WorkOrder::getProductId, WorkOrder::getWorkOrderCode)
                .eq(WorkOrder::getId, workOrderId));
        // 查询产品信息
        Product product = productMapper.selectOne(new LambdaQueryWrapper<Product>()
                .select(Product::getId, Product::getCode, Product::getName)
                .eq(Product::getId, workOrder.getProductId()));
        // 查询工序信息
        Operation operation = operationMapper.selectOne(new LambdaQueryWrapper<Operation>()
                .select(Operation::getId, Operation::getCode, Operation::getName)
                .eq(Operation::getId, operationId));
        // 根据产品ID、工序ID查询互检模板
        List<CheckTemplate> checkTemplateList = templateMapper.selectList(new LambdaQueryWrapper<CheckTemplate>()
                .eq(CheckTemplate::getProductId, workOrder.getProductId())
                .eq(CheckTemplate::getOperationId, workOrder.getWorkOrderCode())
                .eq(CheckTemplate::getCheckType, CheckType.MUTUAL_CHECK));
        if (CollectionUtils.isEmpty(checkTemplateList)) {
            return;
        }
        if (checkTemplateList.size() > 1) {
            throw new BusinessException("产品【" + product.getCode() + "】工序【" + operation.getCode() + "】互检模板数据异常！");
        }
        // 自检模板
        CheckTemplate template = checkTemplateList.get(0);
        // 自检模板明细
        List<CheckTemplateItemRel> relList = templateItemRelMapper.selectList(new LambdaQueryWrapper<CheckTemplateItemRel>()
                .select(CheckTemplateItemRel::getTemplateId, CheckTemplateItemRel::getItemId)
                .eq(CheckTemplateItemRel::getTemplateId, template.getId()));
        List<String> itemIds = relList.stream().map(CheckTemplateItemRel::getItemId).collect(Collectors.toList());
        // 查询自检项
        List<CheckItem> itemList = itemMapper.selectList(new LambdaQueryWrapper<CheckItem>()
                .select(CheckItem::getId, CheckItem::getItemCode, CheckItem::getItemName,
                        CheckItem::getControlType, CheckItem::getMaxValue, CheckItem::getMinValue)
                .eq(CheckItem::getId, itemIds));
        log.info("生成互检质检单开始, workOrderId:{}", workOrderId);
        // 创建检测单
        CheckOrder checkOrder = CheckOrder.buildMutualCheckOrder(product, workOrder, template);
        checkOrderMapper.insert(checkOrder);
        // 创建检测单明细
        List<CheckOrderDetail> orderDetailList = itemList.stream()
                .map(item -> CheckOrderDetail.build(checkOrder, item))
                .collect(Collectors.toList());
        checkOrderDetailService.saveBatch(orderDetailList);
        log.info("生成互检质检单结束, workOrderId:{}", workOrderId);
    }

    @Override
    public void generateSpecialCheckOrder(String workOrderId, String operationId) {
        // 查询工单信息
        WorkOrder workOrder = workOrderMapper.selectOne(new LambdaQueryWrapper<WorkOrder>()
                .select(WorkOrder::getId, WorkOrder::getProductId, WorkOrder::getWorkOrderCode)
                .eq(WorkOrder::getId, workOrderId));
        // 查询产品信息
        Product product = productMapper.selectOne(new LambdaQueryWrapper<Product>()
                .select(Product::getId, Product::getCode, Product::getName)
                .eq(Product::getId, workOrder.getProductId()));
        // 查询工序信息
        Operation operation = operationMapper.selectOne(new LambdaQueryWrapper<Operation>()
                .select(Operation::getId, Operation::getCode, Operation::getName)
                .eq(Operation::getId, operationId));
        // 根据产品ID、工序ID查询专检模板
        List<CheckTemplate> checkTemplateList = templateMapper.selectList(new LambdaQueryWrapper<CheckTemplate>()
                .eq(CheckTemplate::getProductId, workOrder.getProductId())
                .eq(CheckTemplate::getOperationId, workOrder.getWorkOrderCode())
                .eq(CheckTemplate::getCheckType, CheckType.SPECIAL_CHECK));
        if (CollectionUtils.isEmpty(checkTemplateList)) {
            return;
        }
        if (checkTemplateList.size() > 1) {
            throw new BusinessException("产品【" + product.getCode() + "】工序【" + operation.getCode() + "】专检模板数据异常！");
        }
        // 自检模板
        CheckTemplate template = checkTemplateList.get(0);
        // 自检模板明细
        List<CheckTemplateItemRel> relList = templateItemRelMapper.selectList(new LambdaQueryWrapper<CheckTemplateItemRel>()
                .select(CheckTemplateItemRel::getTemplateId, CheckTemplateItemRel::getItemId)
                .eq(CheckTemplateItemRel::getTemplateId, template.getId()));
        List<String> itemIds = relList.stream().map(CheckTemplateItemRel::getItemId).collect(Collectors.toList());
        // 查询自检项
        List<CheckItem> itemList = itemMapper.selectList(new LambdaQueryWrapper<CheckItem>()
                .select(CheckItem::getId, CheckItem::getItemCode, CheckItem::getItemName,
                        CheckItem::getControlType, CheckItem::getMaxValue, CheckItem::getMinValue)
                .eq(CheckItem::getId, itemIds));
        log.info("生成专检质检单开始, workOrderId:{}", workOrderId);
        // 创建检测单
        CheckOrder checkOrder = CheckOrder.buildSpecialCheckOrder(product, workOrder, template);
        checkOrderMapper.insert(checkOrder);
        // 创建检测单明细
        List<CheckOrderDetail> orderDetailList = itemList.stream()
                .map(item -> CheckOrderDetail.build(checkOrder, item))
                .collect(Collectors.toList());
        checkOrderDetailService.saveBatch(orderDetailList);
        log.info("生成专检质检单结束, workOrderId:{}", workOrderId);
    }

    @Override
    public void generateSelfCheckOrder(TaskOrder taskOrder, Product product, Operation operation) {
        // 根据产品ID、工序ID查询自检模板
        List<CheckTemplate> checkTemplateList = templateMapper.selectList(new LambdaQueryWrapper<CheckTemplate>()
                .eq(CheckTemplate::getProductId, product.getId())
                .eq(CheckTemplate::getOperationId, operation.getId())
                .eq(CheckTemplate::getCheckType, CheckType.SELF_CHECK));
        if (CollectionUtils.isEmpty(checkTemplateList)) {
            return;
        }
        if (checkTemplateList.size() > 1) {
            throw new BusinessException("产品【" + product.getCode() + "】工序【" + operation.getCode() + "】自检模板数据异常！");
        }
        // 自检模板
        CheckTemplate template = checkTemplateList.get(0);
        // 自检模板明细
        List<CheckTemplateItemRel> relList = templateItemRelMapper.selectList(new LambdaQueryWrapper<CheckTemplateItemRel>()
                .select(CheckTemplateItemRel::getTemplateId, CheckTemplateItemRel::getItemId)
                .eq(CheckTemplateItemRel::getTemplateId, template.getId()));
        List<String> itemIds = relList.stream().map(CheckTemplateItemRel::getItemId).collect(Collectors.toList());
        // 查询自检项
        List<CheckItem> itemList = itemMapper.selectList(new LambdaQueryWrapper<CheckItem>()
                .select(CheckItem::getId, CheckItem::getItemCode, CheckItem::getItemName,
                        CheckItem::getControlType, CheckItem::getMaxValue, CheckItem::getMinValue)
                .in(CheckItem::getId, itemIds));
        log.info("生成自检质检单开始, taskOrderId:{}", taskOrder.getId());
        // 创建检测单
        CheckOrder checkOrder = CheckOrder.buildSelfCheckOrder(product, taskOrder, template);
        checkOrderMapper.insert(checkOrder);
        // 创建检测单明细
        List<CheckOrderDetail> orderDetailList = itemList.stream()
                .map(item -> CheckOrderDetail.build(checkOrder, item))
                .collect(Collectors.toList());
        checkOrderDetailService.saveBatch(orderDetailList);
        log.info("生成自检质检单结束, taskOrderId:{}", taskOrder.getId());
    }

    @Override
    public void generateMutualCheckOrder(TaskOrder taskOrder, Product product, Operation operation) {
        // 根据产品ID、工序ID查询互检模板
        List<CheckTemplate> checkTemplateList = templateMapper.selectList(new LambdaQueryWrapper<CheckTemplate>()
                .eq(CheckTemplate::getProductId, product.getId())
                .eq(CheckTemplate::getOperationId, operation.getId())
                .eq(CheckTemplate::getCheckType, CheckType.MUTUAL_CHECK));
        if (CollectionUtils.isEmpty(checkTemplateList)) {
            return;
        }
        if (checkTemplateList.size() > 1) {
            throw new BusinessException("产品【" + product.getCode() + "】工序【" + operation.getCode() + "】互检模板数据异常！");
        }
        // 互检模板
        CheckTemplate template = checkTemplateList.get(0);
        // 互检模板明细
        List<CheckTemplateItemRel> relList = templateItemRelMapper.selectList(new LambdaQueryWrapper<CheckTemplateItemRel>()
                .select(CheckTemplateItemRel::getTemplateId, CheckTemplateItemRel::getItemId)
                .eq(CheckTemplateItemRel::getTemplateId, template.getId()));
        List<String> itemIds = relList.stream().map(CheckTemplateItemRel::getItemId).collect(Collectors.toList());
        // 查询互检项
        List<CheckItem> itemList = itemMapper.selectList(new LambdaQueryWrapper<CheckItem>()
                .select(CheckItem::getId, CheckItem::getItemCode, CheckItem::getItemName,
                        CheckItem::getControlType, CheckItem::getMaxValue, CheckItem::getMinValue)
                .in(CheckItem::getId, itemIds));
        log.info("生成互检质检单开始, taskOrderId:{}", taskOrder.getId());
        // 创建检测单
        CheckOrder checkOrder = CheckOrder.buildMutualCheckOrder(product, taskOrder, template);
        checkOrderMapper.insert(checkOrder);
        // 创建检测单明细
        List<CheckOrderDetail> orderDetailList = itemList.stream()
                .map(item -> CheckOrderDetail.build(checkOrder, item))
                .collect(Collectors.toList());
        checkOrderDetailService.saveBatch(orderDetailList);
        log.info("生成互检质检单结束, taskOrderId:{}", taskOrder.getId());
    }

    @Override
    public void generateSpecialCheckOrder(TaskOrder taskOrder, Product product, Operation operation) {
        // 根据产品ID、工序ID查询专检模板
        List<CheckTemplate> checkTemplateList = templateMapper.selectList(new LambdaQueryWrapper<CheckTemplate>()
                .eq(CheckTemplate::getProductId, product.getId())
                .eq(CheckTemplate::getOperationId, operation.getId())
                .eq(CheckTemplate::getCheckType, CheckType.SPECIAL_CHECK));
        if (CollectionUtils.isEmpty(checkTemplateList)) {
            return;
        }
        if (checkTemplateList.size() > 1) {
            throw new BusinessException("产品【" + product.getCode() + "】工序【" + operation.getCode() + "】专检模板数据异常！");
        }
        // 专检模板
        CheckTemplate template = checkTemplateList.get(0);
        // 专检模板明细
        List<CheckTemplateItemRel> relList = templateItemRelMapper.selectList(new LambdaQueryWrapper<CheckTemplateItemRel>()
                .select(CheckTemplateItemRel::getTemplateId, CheckTemplateItemRel::getItemId)
                .eq(CheckTemplateItemRel::getTemplateId, template.getId()));
        List<String> itemIds = relList.stream().map(CheckTemplateItemRel::getItemId).collect(Collectors.toList());
        // 查询专检项
        List<CheckItem> itemList = itemMapper.selectList(new LambdaQueryWrapper<CheckItem>()
                .select(CheckItem::getId, CheckItem::getItemCode, CheckItem::getItemName,
                        CheckItem::getControlType, CheckItem::getMaxValue, CheckItem::getMinValue)
                .in(CheckItem::getId, itemIds));
        log.info("生成专检质检单开始, taskOrderId:{}", taskOrder.getId());
        // 创建检测单
        CheckOrder checkOrder = CheckOrder.buildSpecialCheckOrder(product, taskOrder, template);
        checkOrderMapper.insert(checkOrder);
        // 创建检测单明细
        List<CheckOrderDetail> orderDetailList = itemList.stream()
                .map(item -> CheckOrderDetail.build(checkOrder, item))
                .collect(Collectors.toList());
        checkOrderDetailService.saveBatch(orderDetailList);
        log.info("生成专检质检单结束, taskOrderId:{}", taskOrder.getId());
    }
}
