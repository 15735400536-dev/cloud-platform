package com.maxinhai.platform.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("VO")
public class CheckOrderVO {

    @ApiModelProperty("主键ID")
    private String id;
    /**
     * 检测单编码
     */
    @ApiModelProperty("检测单编码")
    private String orderCode;
    /**
     * 检测模板ID
     */
    @ApiModelProperty("检测模板ID")
    private String checkTemplateId;
    /**
     * 检测类型: 自建、互检、专检
     */
    @ApiModelProperty("检测类型: 自建、互检、专检")
    private String checkType;
    /**
     * 产品ID
     */
    @ApiModelProperty("产品ID")
    private String productId;
    @ApiModelProperty("产品编码")
    private String productCode;
    @ApiModelProperty("产品名称")
    private String productName;

}
