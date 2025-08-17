package com.maxinhai.platform.po.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maxinhai.platform.po.RecordEntity;
import lombok.Data;

/**
 * 产线
 */
@Data
@TableName("mdm_production_line")
public class ProductionLine extends RecordEntity {

    private String code;
    private String name;
    /**
     * 车间ID
     */
    private String workshopId;
}
