package com.maxinhai.platform.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "字典类型VO")
public class DictTypeVO {

    @ApiModelProperty(value = "主键ID")
    private String id;
    /**
     * 字典类型
     */
    private String dictType;
    /**
     * 字典标签
     */
    private String dictLabel;
    /**
     * 状态（1:启用，0:禁用）
     */
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date createTime;
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date updateTime;

}
