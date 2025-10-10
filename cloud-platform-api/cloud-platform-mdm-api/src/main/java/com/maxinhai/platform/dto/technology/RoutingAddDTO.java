package com.maxinhai.platform.dto.technology;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "工艺路线新增DTO")
public class RoutingAddDTO {

    /**
     * 工艺路线编码
     */
    @ApiModelProperty(value = "工艺路线编码")
    private String code;
    /**
     * 工艺路线名称
     */
    @ApiModelProperty(value = "工艺路线名称")
    private String name;
    /**
     * 产品ID
     */
    @ApiModelProperty(value = "产品ID")
    private String productId;
    /**
     * 版本号
     */
    @ApiModelProperty(value = "版本号")
    private String version;
    /**
     * 状态（1:启用，0:禁用）
     */
    @ApiModelProperty(value = "状态（1:启用，0:禁用）")
    private Integer status;

}
