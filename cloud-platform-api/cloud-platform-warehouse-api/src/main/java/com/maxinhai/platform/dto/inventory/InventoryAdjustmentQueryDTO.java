package com.maxinhai.platform.dto.inventory;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.inventory.InventoryAdjustmentVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("DTO")
public class InventoryAdjustmentQueryDTO extends PageSearch<InventoryAdjustmentVO> {

    /**
     * 调整单号
     */
    @ApiModelProperty("调整单号")
    private String adjustmentNo;
    /**
     * 仓库ID
     */
    @ApiModelProperty("仓库ID")
    private String warehouseId;

}
