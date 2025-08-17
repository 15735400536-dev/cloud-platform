package com.maxinhai.platform.dto.inventory;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.inventory.InventoryVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("DTO")
public class InventoryQueryDTO extends PageSearch<InventoryVO> {

    /**
     * 仓库ID
     */
    @ApiModelProperty("仓库ID")
    private String warehouseId;
    /**
     * 库区ID
     */
    @ApiModelProperty("库区ID")
    private String areaId;
    /**
     * 货架ID
     */
    @ApiModelProperty("货架ID")
    private String rackId;
    /**
     * 货位ID
     */
    @ApiModelProperty("货位ID")
    private String locationId;
    /**
     * 物料ID
     */
    @ApiModelProperty("物料ID")
    private String materialId;

}
