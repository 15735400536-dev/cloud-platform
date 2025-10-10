package com.maxinhai.platform.dto.inventory;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.inventory.InventoryVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "库存分页查询DTO")
public class InventoryQueryDTO extends PageSearch<InventoryVO> {

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

}
