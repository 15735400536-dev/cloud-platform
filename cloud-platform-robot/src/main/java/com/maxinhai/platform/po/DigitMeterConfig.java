package com.maxinhai.platform.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @ClassName：DigitMeterConfig
 * @Author: XinHai.Ma
 * @Date: 2026/1/13 22:54
 * @Description: 数字仪表配置
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DigitMeterConfig {

    /**
     * 主键ID
     */
    private String id = UUID.randomUUID().toString().replaceAll("-", "");
    /**
     * 仪表类型
     */
    private String meterType;
    /**
     * 数字仪表-最小刻度
     */
    private String minValue;
    /**
     * 数字仪表-最大刻度
     */
    private String maxValue;
    /**
     * 精度
     */
    private Integer precision;
    /**
     * 单位
     */
    private String unit;

}
