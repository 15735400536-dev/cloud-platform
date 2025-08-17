package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 巡检项目表
 */
@Data
@TableName("dm_inspection_item")
public class InspectionItem extends RecordEntity {

    /**
     * 巡检配置ID
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
     * 项目类型: 定性 定量
     */
    private Integer itemType;
    /**
     * 检查标准
     */
    private String standard;
    /**
     * 检查顺序
     */
    private Integer sort;

}
