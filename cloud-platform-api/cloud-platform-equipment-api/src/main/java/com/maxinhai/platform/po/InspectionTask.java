package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 巡检任务表
 */
@Data
@TableName("dm_inspection_task")
public class InspectionTask extends RecordEntity {

    /**
     * 巡检任务编码
     */
    private String taskCode;
    /**
     * 巡检计划ID
     */
    private String planId;
    /**
     * 设备ID
     */
    private String equipId;
    /**
     * 巡检时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date inspectionDate;
    /**
     * 巡检人
     */
    private String inspector;
    /**
     * 巡检类型
     */
    private String inspectionType;
    /**
     * 巡检结果: 正常 异常
     */
    private String result;
    /**
     * 发现问题
     */
    private String foundProblems;
    /**
     * 处理建议
     */
    private String handlingSuggestion;
    /**
     * 巡检图片
     */
    private String photos;
    /**
     * 备注
     */
    private String remark;

}
