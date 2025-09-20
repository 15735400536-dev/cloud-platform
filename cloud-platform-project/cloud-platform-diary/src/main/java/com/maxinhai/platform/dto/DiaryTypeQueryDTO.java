package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.DiaryTypeVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "DTO")
public class DiaryTypeQueryDTO extends PageSearch<DiaryTypeVO> {

    @ApiModelProperty("日记类型名称")
    private String name;

}
