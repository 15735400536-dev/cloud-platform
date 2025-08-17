package com.maxinhai.platform.dto.inventory;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("DTO")
public class InventoryAdjustmentDetailEditDTO {

    @ApiModelProperty("主键ID")
    private String id;

}
