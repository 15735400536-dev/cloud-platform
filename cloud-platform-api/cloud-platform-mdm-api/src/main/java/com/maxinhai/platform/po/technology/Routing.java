package com.maxinhai.platform.po.technology;

import com.baomidou.mybatisplus.annotation.TableName;
import com.maxinhai.platform.po.RecordEntity;
import lombok.Data;

@Data
@TableName("mdm_routing")
public class Routing extends RecordEntity {

    /**
     * 工艺路线编码
     */
    private String code;
    /**
     * 工艺路线名称
     */
    private String name;
    /**
     * 产品ID
     */
    private String productId;
    /**
     * 版本号
     */
    private String version;
    /**
     * 状态（1:启用，0:禁用）
     */
    private Integer status;

}
