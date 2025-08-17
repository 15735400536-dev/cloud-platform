package com.maxinhai.platform.dto.model;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.model.WarehouseAreaVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("DTO")
public class WarehouseAreaQueryDTO extends PageSearch<WarehouseAreaVO> {

    @ApiModelProperty("编码")
    private String code;
    @ApiModelProperty("名称")
    private String name;
    /**
     * 仓库ID
     */
    @ApiModelProperty("仓库ID")
    private String warehouseId;

}
