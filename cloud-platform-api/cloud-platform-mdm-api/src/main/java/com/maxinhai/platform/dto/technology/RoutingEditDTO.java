package com.maxinhai.platform.dto.technology;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("数据字典新增DTO")
public class RoutingEditDTO {

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
