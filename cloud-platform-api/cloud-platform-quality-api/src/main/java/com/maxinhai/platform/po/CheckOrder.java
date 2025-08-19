package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maxinhai.platform.enums.CheckStatus;
import com.maxinhai.platform.enums.CheckType;
import lombok.Data;

@Data
@TableName("qc_check_order")
public class CheckOrder extends RecordEntity {

    /**
     * 检测单编码
     */
    private String orderCode;
    /**
     * 工单ID
     */
    private String workOrderId;
    /**
     * 派工单ID
     */
    private String taskOrderId;
    /**
     * 检测模板ID
     */
    private String checkTemplateId;
    /**
     * 检测类型: 自建、互检、专检
     */
    private CheckType checkType;
    /**
     * 产品ID
     */
    private String productId;
    /**
     * 检测结果: 合格、不合格
     */
    private String checkResult;
    /**
     * 工序ID
     */
    private String operationId;
    /**
     * 检测状态: 0.待检测 1.已检测
     */
    private CheckStatus status;

    public static CheckOrder buildSelfCheckOrder(Product product, WorkOrder workOrder, CheckTemplate template) {
        CheckOrder checkOrder = new CheckOrder();
        checkOrder.setWorkOrderId(workOrder.getId());
        checkOrder.setOrderCode(workOrder.getWorkOrderCode());
        checkOrder.setCheckTemplateId(template.getId());
        checkOrder.setCheckType(CheckType.SELF_CHECK);
        checkOrder.setProductId(product.getId());
        checkOrder.setStatus(CheckStatus.NO);
        return checkOrder;
    }

    public static CheckOrder buildMutualCheckOrder(Product product, WorkOrder workOrder, CheckTemplate template) {
        CheckOrder checkOrder = new CheckOrder();
        checkOrder.setWorkOrderId(workOrder.getId());
        checkOrder.setOrderCode(workOrder.getWorkOrderCode());
        checkOrder.setCheckTemplateId(template.getId());
        checkOrder.setCheckType(CheckType.MUTUAL_CHECK);
        checkOrder.setProductId(product.getId());
        checkOrder.setStatus(CheckStatus.NO);
        return checkOrder;
    }

    public static CheckOrder buildSpecialCheckOrder(Product product, WorkOrder workOrder, CheckTemplate template) {
        CheckOrder checkOrder = new CheckOrder();
        checkOrder.setWorkOrderId(workOrder.getId());
        checkOrder.setOrderCode(workOrder.getWorkOrderCode());
        checkOrder.setCheckTemplateId(template.getId());
        checkOrder.setCheckType(CheckType.SPECIAL_CHECK);
        checkOrder.setProductId(product.getId());
        checkOrder.setStatus(CheckStatus.NO);
        return checkOrder;
    }

    public static CheckOrder buildSelfCheckOrder(Product product, TaskOrder taskOrder, CheckTemplate template) {
        CheckOrder checkOrder = new CheckOrder();
        checkOrder.setTaskOrderId(taskOrder.getId());
        checkOrder.setOrderCode(taskOrder.getTaskOrderCode());
        checkOrder.setOperationId(taskOrder.getOperationId());
        checkOrder.setCheckTemplateId(template.getId());
        checkOrder.setCheckType(CheckType.SELF_CHECK);
        checkOrder.setProductId(product.getId());
        checkOrder.setStatus(CheckStatus.NO);
        return checkOrder;
    }

    public static CheckOrder buildMutualCheckOrder(Product product, TaskOrder taskOrder, CheckTemplate template) {
        CheckOrder checkOrder = new CheckOrder();
        checkOrder.setTaskOrderId(taskOrder.getId());
        checkOrder.setOrderCode(taskOrder.getTaskOrderCode());
        checkOrder.setOperationId(taskOrder.getOperationId());
        checkOrder.setCheckTemplateId(template.getId());
        checkOrder.setCheckType(CheckType.MUTUAL_CHECK);
        checkOrder.setProductId(product.getId());
        checkOrder.setStatus(CheckStatus.NO);
        return checkOrder;
    }

    public static CheckOrder buildSpecialCheckOrder(Product product, TaskOrder taskOrder, CheckTemplate template) {
        CheckOrder checkOrder = new CheckOrder();
        checkOrder.setTaskOrderId(taskOrder.getId());
        checkOrder.setOrderCode(taskOrder.getTaskOrderCode());
        checkOrder.setOperationId(taskOrder.getOperationId());
        checkOrder.setCheckTemplateId(template.getId());
        checkOrder.setCheckType(CheckType.SPECIAL_CHECK);
        checkOrder.setProductId(product.getId());
        checkOrder.setStatus(CheckStatus.NO);
        return checkOrder;
    }

}
