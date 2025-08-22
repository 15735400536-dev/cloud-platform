package com.maxinhai.platform.dto.stocktaking;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.stocktaking.StocktakingDetailVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "DTO")
public class StocktakingDetailQueryDTO extends PageSearch<StocktakingDetailVO> {

    /**
     * 盘点单ID
     */
    @ApiModelProperty(value = "盘点单ID")
    private String stocktakingId;

}
