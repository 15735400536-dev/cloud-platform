package com.maxinhai.platform.dto.inventory;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.inventory.InventoryFlowVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName：InventoryFlowQueryDTO
 * @Author: XinHai.Ma
 * @Date: 2025/8/21 9:55
 * @Description: 库存流水分页查询DTO
 */
@Data
@ApiModel(description = "库存流水分页查询DTO")
public class InventoryFlowQueryDTO extends PageSearch<InventoryFlowVO> {

    /**
     * 仓库ID
     */
    @ApiModelProperty(value = "仓库ID")
    private String warehouseId;
    /**
     * 库区ID
     */
    @ApiModelProperty(value = "库区ID")
    private String areaId;
    /**
     * 货架ID
     */
    @ApiModelProperty(value = "货架ID")
    private String rackId;
    /**
     * 货位ID
     */
    @ApiModelProperty(value = "货位ID")
    private String locationId;
    /**
     * 物料ID
     */
    @ApiModelProperty(value = "物料ID")
    private String materialId;
    /**
     * 批次号
     */
    @ApiModelProperty(value = "批次号")
    private String batchNo;

}
