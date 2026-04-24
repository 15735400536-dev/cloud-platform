package com.maxinhai.platform.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "派工单工序BO")
public class TaskOrderProcessSortBO {

    @ApiModelProperty(value = "工序顺序号")
    private Integer sort;
    @ApiModelProperty(value = "派工单数量")
    private Integer count;

}
