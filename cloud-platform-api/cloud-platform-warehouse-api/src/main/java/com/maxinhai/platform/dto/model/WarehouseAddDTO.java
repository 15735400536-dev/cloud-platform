package com.maxinhai.platform.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("DTO")
public class WarehouseAddDTO {

    /**
     * 仓库编码
     */
    @ApiModelProperty("仓库编码")
    private String code;
    /**
     * 仓库名称
     */
    @ApiModelProperty("仓库编码")
    private String name;
    /**
     * 状态：0-禁用，1-启用
     */
    @ApiModelProperty("仓库编码")
    private Integer status;
    /**
     * 仓库地址
     */
    @ApiModelProperty("仓库地址")
    private String address;
    /**
     * 联系人
     */
    @ApiModelProperty("联系人")
    private String contactPerson;
    /**
     * 联系电话
     */
    @ApiModelProperty("联系电话")
    private String contactPhone;
    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

}
