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
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
@ApiModel("DTO")
public class InventoryFlowQueryDTO extends PageSearch<InventoryFlowVO> {

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
    /**
     * 批次号
     */
    @ApiModelProperty("批次号")
    private String batchNo;

}
