package com.maxinhai.platform.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "产品VO")
public class ProductVO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 产品编码
     */
    @ApiModelProperty(value = "产品编码")
    private String code;
    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称")
    private String name;
    /**
     * 产品描述
     */
    @ApiModelProperty(value = "产品描述")
    private String description;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date createTime;
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date updateTime;

}
