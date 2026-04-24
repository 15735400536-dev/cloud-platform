package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.InfoPublishVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "信息发布分页查询DTO")
public class InfoPublishQueryDTO extends PageSearch<InfoPublishVO> {

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;

}
