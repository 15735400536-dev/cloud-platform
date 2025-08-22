package com.maxinhai.platform.dto.model;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.model.ProductionLineVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "分页查询DTO")
public class ProductionLineQueryDTO extends PageSearch<ProductionLineVO> {

    @ApiModelProperty(value = "产线编码")
    private String code;
    @ApiModelProperty(value = "产线名称")
    private String name;

}
