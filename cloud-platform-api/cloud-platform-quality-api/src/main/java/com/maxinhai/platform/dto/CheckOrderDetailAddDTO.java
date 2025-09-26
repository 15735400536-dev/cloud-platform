package com.maxinhai.platform.dto;

import com.maxinhai.platform.enums.CheckStatus;
import com.maxinhai.platform.enums.ControlType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(description = "检测单明细新增DTO")
public class CheckOrderDetailAddDTO {

    /**
     * 检测单ID
     */
    @ApiModelProperty(value = "检测单ID")
    private String checkOrderId;
    /**
     * 检测项ID
     */
    @ApiModelProperty(value = "检测项ID")
    private String checkItemId;
    /**
     * 检测项编码
     */
    @ApiModelProperty(value = "检测项编码")
    private String itemCode;
    /**
     * 检测项名称
     */
    @ApiModelProperty(value = "主键ID")
    private String itemName;
    /**
     * 检测类型: 定性、定量、手动输入
     */
    @ApiModelProperty(value = "检测类型: 定性、定量、手动输入")
    private ControlType controlType;
    /**
     * 最小值
     */
    @ApiModelProperty(value = "最小值")
    private BigDecimal minValue;
    /**
     * 最大值
     */
    @ApiModelProperty(value = "最大值")
    private BigDecimal maxValue;
    /**
     * 检测值
     */
    @ApiModelProperty(value = "检测值")
    private BigDecimal checkValue;
    /**
     * 检测结果: 合格、不合格
     */
    @ApiModelProperty(value = "检测结果: 合格、不合格")
    private String checkResult;
    /**
     * 检测状态: 0.待检测 1.已检测
     */
    @ApiModelProperty(value = "检测状态: 0.待检测 1.已检测")
    private CheckStatus status;

}
