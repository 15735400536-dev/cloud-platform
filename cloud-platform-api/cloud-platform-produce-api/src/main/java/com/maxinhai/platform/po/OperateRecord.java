package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.maxinhai.platform.enums.OperateType;
import lombok.Data;

import java.util.Date;

@Data
@TableName("prod_operate_record")
public class OperateRecord extends RecordEntity {

    /**
     * 派工单ID
     */
    private String taskOrderId;
    /**
     * 操作类型
     */
    private OperateType operateType;
    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date operateTime;

}
