package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 巡检计划表
 */
@Data
@TableName("dm_inspection_config")
public class InspectionPlan extends RecordEntity {

    /**
     * 计划编码
     */
    private String planCode;
    /**
     * 巡检配置ID
     */
    private String configId;
    /**
     * 设备ID
     */
    private String equipId;
    /**
     * 计划日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date planDate;
    /**
     * 计划开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date planBeginTime;
    /**
     * 计划结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date planEndTime;
    /**
     * 计划巡检人
     */
    private String inspector;
    /**
     * 计划状态: 未执行 执行中 已完成 已取消
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;

}
