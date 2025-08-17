package com.maxinhai.platform.po.technology;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maxinhai.platform.po.RecordEntity;
import lombok.Data;

@Data
@TableName("mdm_bom")
public class Bom extends RecordEntity {

    /**
     * BOM编码
     */
    private String code;
    /**
     * BOM名称
     */
    private String name;
    /**
     * 版本号
     */
    private String version;
    /**
     * BOM描述
     */
    private String description;
    /**
     * 产品ID
     */
    private String productId;

}
