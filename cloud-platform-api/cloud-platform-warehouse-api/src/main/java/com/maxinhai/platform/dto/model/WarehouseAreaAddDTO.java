package com.maxinhai.platform.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("DTO")
public class WarehouseAreaAddDTO {

    /**
     * 仓库ID
     */
    @ApiModelProperty("仓库ID")
    private String warehouseId;
    /**
     * 库区编码
     */
    @ApiModelProperty("库区编码")
    private String code;
    /**
     * 库区名称
     */
    @ApiModelProperty("库区名称")
    private String name;
    /**
     * 状态：0-禁用，1-启用
     */
    @ApiModelProperty("状态：0-禁用，1-启用")
    private Integer status;
    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

}
