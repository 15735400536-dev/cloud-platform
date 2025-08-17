package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maxinhai.platform.enums.ControlType;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("qc_check_item")
public class CheckItem extends RecordEntity {

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
