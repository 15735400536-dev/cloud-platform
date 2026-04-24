package com.maxinhai.platform.vo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "工厂VO")
public class FactoryVO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    @ApiModelProperty(value = "编码")
    private String code;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "工厂地址")
    private String address;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date createTime;
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date updateTime;

}
