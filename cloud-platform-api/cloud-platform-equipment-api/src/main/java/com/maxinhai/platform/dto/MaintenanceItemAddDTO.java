package com.maxinhai.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "新增DTO")
public class MaintenanceItemAddDTO {

    /**
     * 保养配置ID
     */
    @ApiModelProperty("保养配置ID")
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
     * 保养内容
     */
    @ApiModelProperty("保养内容")
    private String content;
    /**
     * 保养标准
     */
    @ApiModelProperty("保养标准")
    private String standard;
    /**
     * 保养顺序
     */
    @ApiModelProperty("保养顺序")
    private Integer sort;
    /**
     * 所需备件
     */
    @ApiModelProperty("所需备件")
    private String requiredParts;

}
