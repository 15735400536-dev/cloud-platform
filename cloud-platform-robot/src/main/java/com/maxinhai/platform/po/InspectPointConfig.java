package com.maxinhai.platform.po;

import com.maxinhai.platform.enums.AlarmConditionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @ClassName：InspectPointConfig
 * @Author: XinHai.Ma
 * @Date: 2026/1/13 22:40
 * @Description: 巡检点位配置
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InspectPointConfig {

    /**
     * 主键ID
     */
    private String id = UUID.randomUUID().toString().replaceAll("-", "");
    /**
     * 巡检点位ID
     */
    private String pointId;
    /**
     * 算法ID
     */
    private String algorithmId;
    /**
     * 指针/数字仪表配置ID
     */
    private String configId;
    /**
     * 告警条件：大于 大于等于 小于 小于等于 等于 不等于 在范围 不在范围
     */
    private AlarmConditionEnum condition;
    /**
     * 告警条件-下限
     */
    private BigDecimal minValue;
    /**
     * 告警条件-上限
     */
    private BigDecimal maxValue;
    /**
     * 告警条件-标准值
     */
    private BigDecimal standardValue;

}
