package com.maxinhai.platform.dto.model;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.model.WorkCellVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("分页查询DTO")
public class WorkCellQueryDTO extends PageSearch<WorkCellVO> {

    @ApiModelProperty("编码")
    private String code;
    @ApiModelProperty("名称")
    private String name;

}
