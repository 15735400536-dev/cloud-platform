package com.maxinhai.platform.dto;

import com.maxinhai.platform.enums.CheckStatus;
import com.maxinhai.platform.enums.ControlType;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("DTO")
public class CheckOrderDetailAddDTO {

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

}
