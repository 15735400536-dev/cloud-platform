package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.CheckTemplateVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "检测模板分页查询DTO")
public class CheckTemplateQueryDTO extends PageSearch<CheckTemplateVO> {

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
    private String checkType;
    /**
     * 产品编码
     */
    @ApiModelProperty(value = "产品编码")
    private String productCode;
    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称")
    private String productName;
}
