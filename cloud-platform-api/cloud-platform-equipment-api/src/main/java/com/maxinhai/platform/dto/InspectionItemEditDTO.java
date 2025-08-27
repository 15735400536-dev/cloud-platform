package com.maxinhai.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "编辑DTO")
public class InspectionItemEditDTO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 巡检配置ID
     */
    @ApiModelProperty("巡检配置ID")
    private String configId;
    /**
     * 项目编码
     */
    @ApiModelProperty("项目编码")
    private String itemCode;
    /**
     * 项目名称
     */
    @ApiModelProperty("项目名称")
    private String itemName;
    /**
     * 项目类型: 定性 定量
     */
    @ApiModelProperty("项目类型: 定性 定量")
    private Integer itemType;
    /**
     * 检查标准
     */
    @ApiModelProperty("检查标准")
    private String standard;
    /**
     * 检查顺序
     */
    @ApiModelProperty("检查顺序")
    private Integer sort;

}
