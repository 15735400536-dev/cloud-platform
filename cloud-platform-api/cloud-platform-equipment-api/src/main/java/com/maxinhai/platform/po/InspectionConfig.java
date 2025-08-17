package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 巡检配置表
 */
@Data
@TableName("dm_inspection_config")
public class InspectionConfig extends RecordEntity {

    /**
     * 配置编码
     */
    private String configCode;
    /**
     * 配置名称
     */
    private String configName;
    /**
     * 设备ID
     */
    private String equipId;
    /**
     * 巡检类型: 日常巡检 专项巡检 季度巡检 年度巡检
     */
    private String inspectionType;
    /**
     * 周期类型: 日 周 月 季度 年
     */
    private Integer cycleType;
    /**
     * 周期间隔
     */
    private Integer cycleInterval;
    /**
     * 状态: 1.启用 0.禁用
     */
    private Integer status;

}
