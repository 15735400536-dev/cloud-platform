package com.maxinhai.platform.po.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maxinhai.platform.po.RecordEntity;
import lombok.Data;

@Data
@TableName("mdm_work_cell")
public class WorkCell extends RecordEntity {

    private String code;
    private String name;
    /**
     * 加工中心ID
     */
    private String workCenterId;
    /**
     * 产线ID
     */
    private String productionLineId;

}
