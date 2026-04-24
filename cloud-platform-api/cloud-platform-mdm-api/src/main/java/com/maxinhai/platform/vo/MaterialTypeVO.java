package com.maxinhai.platform.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "物料类型VO")
public class MaterialTypeVO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    @ApiModelProperty(value = "物料类型编码")
    private String code;
    @ApiModelProperty(value = "物料类型名称")
    private String name;
    @ApiModelProperty(value = "物料类型描述")
    private String description;
    @ApiModelProperty(value = "父级ID")
    private String parentId;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date createTime;
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date updateTime;

}
