package com.maxinhai.platform.dto.inventory;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "调整单明细编辑DTO")
public class InventoryAdjustmentDetailEditDTO {

    @ApiModelProperty(value = "主键ID")
    private String id;

}
