package com.maxinhai.platform.dto.inventory;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "DTO")
public class InventoryAdjustmentDetailEditDTO {

    @ApiModelProperty(value = "主键ID")
    private String id;

}
