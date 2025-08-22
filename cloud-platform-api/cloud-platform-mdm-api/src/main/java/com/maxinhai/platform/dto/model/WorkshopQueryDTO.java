package com.maxinhai.platform.dto.model;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.model.WorkshopVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "车间分页查询DTO")
public class WorkshopQueryDTO extends PageSearch<WorkshopVO> {

    @ApiModelProperty(value = "车间编码")
    private String code;
    @ApiModelProperty(value = "车间名称")
    private String name;

}
