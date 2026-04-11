package com.maxinhai.platform.vo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "仓库VO")
public class WarehouseVO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    @ApiModelProperty(value = "仓库编码")
    private String code;
    @ApiModelProperty(value = "仓库名称")
    private String name;

    /**
     * 仓库类型：PT-普通总仓、LS-线边仓、YL-原料仓、CP-成品仓、BJ-备件仓、WH-危化仓
     */
    @ApiModelProperty(value = "仓库类型：PT-普通总仓、LS-线边仓、YL-原料仓、CP-成品仓、BJ-备件仓、WH-危化仓")
    private String type;

    /**
     * 状态：0-禁用，1-启用
     */
    @ApiModelProperty(value = "状态：0-禁用，1-启用")
    private Integer status;
    /**
     * 仓库地址
     */
    @ApiModelProperty(value = "仓库地址")
    private String address;
    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    private String contactPerson;
    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话")
    private String contactPhone;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date createTime;
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date updateTime;

}
