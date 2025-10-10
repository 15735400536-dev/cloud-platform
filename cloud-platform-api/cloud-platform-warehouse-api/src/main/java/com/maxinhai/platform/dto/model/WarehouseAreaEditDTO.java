package com.maxinhai.platform.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "库区编辑DTO")
public class WarehouseAreaEditDTO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 仓库ID
     */
    @ApiModelProperty(value = "仓库ID")
    private String warehouseId;
    /**
     * 库区编码
     */
    @ApiModelProperty(value = "库区编码")
    private String code;
    /**
     * 库区名称
     */
    @ApiModelProperty(value = "库区名称")
    private String name;
    /**
     * 状态：0-禁用，1-启用
     */
    @ApiModelProperty(value = "状态：0-禁用，1-启用")
    private Integer status;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

}
