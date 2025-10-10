package com.maxinhai.platform.dto;

import com.maxinhai.platform.enums.CheckType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "检测模板编辑DTO")
public class CheckTemplateEditDTO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 检测模板编码
     */
    @ApiModelProperty(value = "检测模板编码")
    private String templateCode;
    /**
     * 检测模板名称
     */
    @ApiModelProperty(value = "检测模板名称")
    private String templateName;
    /**
     * 检测类型: 自建、互检、专检
     */
    @ApiModelProperty(value = "检测类型: 自建、互检、专检")
    private CheckType checkType;
    /**
     * 产品ID
     */
    @ApiModelProperty(value = "产品ID")
    private String productId;
    /**
     * 工序ID
     */
    @ApiModelProperty(value = "工序ID")
    private String operationId;

}
