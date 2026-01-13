package com.maxinhai.platform.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @ClassName：MeterTypeConfig
 * @Author: XinHai.Ma
 * @Date: 2026/1/13 22:49
 * @Description: 指针仪表配置
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointerMeterConfig {

    /**
     * 主键ID
     */
    private String id = UUID.randomUUID().toString().replaceAll("-", "");
    /**
     * 仪表类型
     */
    private String meterType;
    /**
     * 指针名称
     */
    private String name;
    /**
     * 指针仪表-最小刻度
     */
    private String minValue;
    /**
     * 指针仪表-最大刻度
     */
    private String maxValue;
    /**
     * 刻度
     */
    private Integer scale;
    /**
     * 单位
     */
    private String unit;

}
