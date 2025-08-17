package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 故障代码表
 */
@Data
@TableName("dm_fault_code")
public class FaultCode extends RecordEntity {

    /**
     * 故障代码
     */
    private String faultCode;
    /**
     * 故障名称
     */
    private String faultName;
    /**
     * 故障描述
     */
    private String description;
    /**
     * 可能原因
     */
    private String possibleCauses;
    /**
     * 解决方案
     */
    private String solution;

}
