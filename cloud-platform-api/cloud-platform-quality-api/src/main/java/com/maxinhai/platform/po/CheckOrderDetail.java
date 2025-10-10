package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maxinhai.platform.bo.CheckTemplateItemBO;
import com.maxinhai.platform.enums.CheckStatus;
import com.maxinhai.platform.enums.ControlType;
import lombok.Data;
import org.springframework.transaction.annotation.SpringTransactionAnnotationParser;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("qc_check_order_detail")
public class CheckOrderDetail extends RecordEntity {

    /**
     * 检测单ID
     */
    private String checkOrderId;
    /**
     * 检测项ID
     */
    private String checkItemId;
    /**
     * 检测项编码
     */
    private String itemCode;
    /**
     * 检测项名称
     */
    private String itemName;
    /**
     * 检测类型: 定性、定量、手动输入
     */
    private ControlType controlType;
    /**
     * 最小值
     */
    private BigDecimal minValue;
    /**
     * 最大值
     */
    private BigDecimal maxValue;
    /**
     * 检测值
     */
    private BigDecimal checkValue;
    /**
     * 检测结果: 合格、不合格
     */
    private String checkResult;
    /**
     * 检测状态: 0.待检测 1.已检测
     */
    private CheckStatus status;

    public static CheckOrderDetail build(CheckOrder checkOrder, CheckItem item) {
        CheckOrderDetail checkOrderDetail = new CheckOrderDetail();
        checkOrderDetail.setCheckOrderId(checkOrder.getId());
        checkOrderDetail.setCheckItemId(item.getId());
        checkOrderDetail.setItemCode(item.getItemCode());
        checkOrderDetail.setItemName(item.getItemName());
        checkOrderDetail.setControlType(item.getControlType());
        checkOrderDetail.setMinValue(item.getMinValue());
        checkOrderDetail.setMaxValue(item.getMaxValue());
        checkOrderDetail.setStatus(CheckStatus.NO);
        return checkOrderDetail;
    }

    public static CheckOrderDetail build(CheckOrder checkOrder, CheckTemplateItemBO itemBO) {
        CheckOrderDetail checkOrderDetail = new CheckOrderDetail();
        checkOrderDetail.setCheckOrderId(checkOrder.getId());
        checkOrderDetail.setCheckItemId(itemBO.getItemId());
        checkOrderDetail.setItemCode(itemBO.getItemCode());
        checkOrderDetail.setItemName(itemBO.getItemName());
        checkOrderDetail.setControlType(itemBO.getControlType());
        checkOrderDetail.setMinValue(itemBO.getMinValue());
        checkOrderDetail.setMaxValue(itemBO.getMaxValue());
        checkOrderDetail.setStatus(CheckStatus.NO);
        return checkOrderDetail;
    }

}
