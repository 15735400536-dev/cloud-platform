package com.maxinhai.platform.dto.inventory;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.inventory.InventoryAdjustmentDetailVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("DTO")
public class InventoryAdjustmentDetailQueryDTO extends PageSearch<InventoryAdjustmentDetailVO> {

    /**
     * 调整单ID
     */
    @ApiModelProperty("调整单号")
    private String adjustmentId;

}
