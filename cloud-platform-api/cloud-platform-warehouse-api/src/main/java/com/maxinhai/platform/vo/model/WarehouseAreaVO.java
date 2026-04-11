package com.maxinhai.platform.vo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "库区VO")
public class WarehouseAreaVO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    @ApiModelProperty(value = "编码")
    private String code;
    @ApiModelProperty(value = "名称")
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
    /**
     * 仓库ID
     */
    @ApiModelProperty(value = "仓库ID")
    private String warehouseId;
    @ApiModelProperty(value = "仓库编码")
    private String warehouseCode;
    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date createTime;
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date updateTime;

}
