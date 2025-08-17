package com.maxinhai.platform.po.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maxinhai.platform.po.RecordEntity;
import lombok.Data;

/**
 * 加工中心
 */
@Data
@TableName("mdm_work_center")
public class WorkCenter extends RecordEntity {

    /**
     * 加工中心编码
     */
    private String code;
    /**
     * 加工中心名称
     */
    private String name;
    /**
     * 车间ID
     */
    private String workshopId;

}
