package com.maxinhai.platform.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "VO")
public class DiaryVO {

    @ApiModelProperty("主键ID")
    private String id;
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("内容")
    private String content;
    @ApiModelProperty("日记类型ID")
    private String diaryTypeId;
    @ApiModelProperty("日记类型名称")
    private String diaryTypeName;

}
