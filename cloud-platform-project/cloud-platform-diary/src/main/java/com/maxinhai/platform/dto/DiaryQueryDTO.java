package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.DiaryVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "DTO")
public class DiaryQueryDTO extends PageSearch<DiaryVO> {

    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("日记类型ID")
    private String diaryTypeId;

}
