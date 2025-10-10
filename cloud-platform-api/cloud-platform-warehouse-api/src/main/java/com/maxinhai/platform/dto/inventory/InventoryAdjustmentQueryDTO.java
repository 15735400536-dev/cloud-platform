package com.maxinhai.platform.dto.inventory;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.inventory.InventoryAdjustmentVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "调整单分页查询DTO")
public class InventoryAdjustmentQueryDTO extends PageSearch<InventoryAdjustmentVO> {

    /**
     * 调整单号
     */
    @ApiModelProperty(value = "调整单号")
    private String adjustmentNo;
    /**
     * 仓库ID
     */
    @ApiModelProperty(value = "仓库ID")
    private String warehouseId;

}
