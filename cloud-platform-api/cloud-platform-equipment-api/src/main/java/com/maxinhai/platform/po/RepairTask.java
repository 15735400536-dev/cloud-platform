package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 维修任务
 */
@Data
@TableName("dm_equip_repair")
public class RepairTask extends RecordEntity {

    /**
     * 维修任务编码
     */
    private String taskCode;
    /**
     * 设备ID
     */
    private String equipId;
    /**
     * 故障编码
     */
    private String faultCode;
    /**
     * 故障发生时间
     */
    private Date faultTime;
    /**
     * 保修时间
     */
    private Date reportTime;
    /**
     * 报修人
     */
    private String reporter;
    /**
     * 报修人联系方式
     */
    private String reporterContact;

}
