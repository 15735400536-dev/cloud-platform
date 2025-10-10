package com.maxinhai.platform.dto.inventory;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.inventory.InventoryAdjustmentDetailVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "调整单明细分页查询DTO")
public class InventoryAdjustmentDetailQueryDTO extends PageSearch<InventoryAdjustmentDetailVO> {

    /**
     * 调整单ID
     */
    @ApiModelProperty(value = "调整单号")
    private String adjustmentId;

}
