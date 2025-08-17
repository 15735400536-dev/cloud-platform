package com.maxinhai.platform.po.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maxinhai.platform.po.RecordEntity;
import lombok.Data;

/**
 * 车间
 */
@Data
@TableName("mdm_workshop")
public class Workshop extends RecordEntity {

    private String code;
    private String name;
    private String factoryId;

}
