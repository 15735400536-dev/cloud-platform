package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 *  保养项目表
 */
@Data
@TableName("dm_maintenance_item")
public class MaintenanceItem extends RecordEntity {

    /**
     * 保养配置ID
     */
    private String configId;
    /**
     * 项目编码
     */
    private String itemCode;
    /**
     * 项目名称
     */
    private String itemName;
    /**
     * 保养内容
     */
    private String content;
    /**
     * 保养标准
     */
    private String standard;
    /**
     * 保养顺序
     */
    private Integer sort;
    /**
     * 所需备件
     */
    private String requiredParts;


}
