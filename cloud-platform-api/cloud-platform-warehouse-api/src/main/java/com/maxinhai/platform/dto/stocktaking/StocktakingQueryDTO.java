package com.maxinhai.platform.dto.stocktaking;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.stocktaking.StocktakingVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("DTO")
public class StocktakingQueryDTO extends PageSearch<StocktakingVO> {

    /**
     *  盘点单号
     */
    @ApiModelProperty("盘点单号")
    private String stocktakingNo;
    /**
     *  仓库ID
     */
    @ApiModelProperty("仓库ID")
    private String warehouseId;

}
