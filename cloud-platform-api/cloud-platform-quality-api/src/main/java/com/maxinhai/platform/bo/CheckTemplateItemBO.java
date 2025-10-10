package com.maxinhai.platform.bo;

import com.maxinhai.platform.enums.CheckType;
import com.maxinhai.platform.enums.ControlType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CheckTemplateItemBO {

    /**
     * 检测模板ID
     */
    private String templateId;
    /**
     * 检测类型: 自建、互检、专检
     */
    private CheckType checkType;
    /**
     * 工序ID
     */
    private String operationId;
    /**
     * 工序编码
     */
    private String operationCode;
    /**
     * 检测项ID
     */
    private String itemId;
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

}
