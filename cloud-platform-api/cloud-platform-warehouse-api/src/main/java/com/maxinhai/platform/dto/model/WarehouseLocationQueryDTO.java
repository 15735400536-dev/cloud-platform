package com.maxinhai.platform.dto.model;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.model.WarehouseLocationVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "DTO")
public class WarehouseLocationQueryDTO extends PageSearch<WarehouseLocationVO> {

    /**
     * 货位编码
     */
    @ApiModelProperty(value = "货位编码")
    private String code;
    /**
     * 货位名称
     */
    @ApiModelProperty(value = "货位名称")
    private String name;

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

}
