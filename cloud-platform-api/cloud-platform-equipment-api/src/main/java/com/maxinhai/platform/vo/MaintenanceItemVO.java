package com.maxinhai.platform.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "保养项目VO")
public class MaintenanceItemVO {

    @ApiModelProperty(value = "主键ID")
    private String id;
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
