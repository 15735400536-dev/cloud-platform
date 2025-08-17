package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_dict_type")
public class DictType extends RecordEntity {

    /**
     * 字典类型
     */
    private String dictType;
    /**
     * 字典标签
     */
    private String dictLabel;
    /**
     * 状态（1:启用，0:禁用）
     */
    private Integer status;

}
