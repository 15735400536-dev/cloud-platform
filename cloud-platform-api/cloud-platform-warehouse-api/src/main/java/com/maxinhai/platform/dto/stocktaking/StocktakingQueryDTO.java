package com.maxinhai.platform.dto.stocktaking;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.stocktaking.StocktakingVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "盘点单分页查询DTO")
public class StocktakingQueryDTO extends PageSearch<StocktakingVO> {

    /**
     *  盘点单号
     */
    @ApiModelProperty(value = "盘点单号")
    private String stocktakingNo;
    /**
     *  仓库ID
     */
    @ApiModelProperty(value = "仓库ID")
    private String warehouseId;

}
