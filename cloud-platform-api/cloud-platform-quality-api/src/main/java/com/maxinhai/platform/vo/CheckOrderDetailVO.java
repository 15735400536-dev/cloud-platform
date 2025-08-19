package com.maxinhai.platform.vo;

import com.maxinhai.platform.enums.CheckStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("VO")
public class CheckOrderDetailVO {

    @ApiModelProperty("主键ID")
    private String id;
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
     * 检测类型: 定性 定量 手动输入
     */
    private String checkType;
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

}
