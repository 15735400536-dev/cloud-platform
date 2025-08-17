package com.maxinhai.platform.vo.technology;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("数据字典新增DTO")
public class RoutingVO {

    @ApiModelProperty("主键ID")
    private String id;
    /**
     * 工艺路线编码
     */
    @ApiModelProperty("工艺路线编码")
    private String code;
    /**
     * 工艺路线名称
     */
    @ApiModelProperty("工艺路线名称")
    private String name;
    /**
     * 产品ID
     */
    @ApiModelProperty("产品ID")
    private String productId;
    @ApiModelProperty("产品编码")
    private String productCode;
    @ApiModelProperty("产品名称")
    private String productName;
    /**
     * 版本号
     */
    @ApiModelProperty("版本号")
    private String version;
    /**
     * 状态（1:启用，0:禁用）
     */
    @ApiModelProperty("状态（1:启用，0:禁用）")
    private Integer status;

}
