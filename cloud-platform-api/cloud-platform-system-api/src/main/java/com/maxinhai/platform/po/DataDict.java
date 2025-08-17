package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_data_dict")
public class DataDict extends RecordEntity {

    /**
     * 字典类型
     */
    private String dictType;
    /**
     * 字典标识
     */
    private String dictKey;
    /**
     * 字典数值
     */
    private String dictValue;
    /**
     * 排序字段
     */
    private Integer sort;
    /**
     * 状态（1:启用，0:禁用）
     */
    private Integer status;

}
