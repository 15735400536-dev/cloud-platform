package com.maxinhai.platform.po.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maxinhai.platform.po.RecordEntity;
import lombok.Data;

/**
 * 工厂
 */
@Data
@TableName("mdm_factory")
public class Factory extends RecordEntity {

    private String code;
    private String name;
    private String address;

}
