package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 保养任务表
 */
@Data
@TableName("dm_maintenance_task")
public class MaintenanceTask extends RecordEntity {

    /**
     * 保养任务编码
     */
    private String taskCode;
    /**
     * 保养类型
     */
    private String maintenanceType;
    /**
     * 保养等级
     */
    private String maintenanceLevel;
    /**
     * 巡检计划ID
     */
    private String planId;
    /**
     * 设备ID
     */
    private String equipId;
    /**
     * 实际保养期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date actualDate;
    /**
     * 实际开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date actualBeginTime;
    /**
     * 实际结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date actualEndTime;
    /**
     * 实际保养人
     */
    private String maintainer;
    /**
     * 计划状态: 未执行 执行中 已完成 已取消
     */
    private Integer status;
    /**
     * 保养结果: 合格 不合格 需返工
     */
    private Integer result;
    /**
     * 更换部件
     */
    private String replacedParts;
    /**
     * 保养费用
     */
    private BigDecimal cost;
    /**
     * 验收人
     */
    private String acceptanceBy;
    /**
     * 验收时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date acceptanceTime;
    /**
     * 验收照片
     */
    private String photos;
    /**
     * 备注
     */
    private String remark;


}
