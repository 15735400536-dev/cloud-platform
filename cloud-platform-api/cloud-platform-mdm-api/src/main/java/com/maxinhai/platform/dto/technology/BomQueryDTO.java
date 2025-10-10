package com.maxinhai.platform.dto.technology;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.technology.BomVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "物料BOM分页查询DTO")
public class BomQueryDTO extends PageSearch<BomVO> {

    /**
     * BOM编码
     */
    @ApiModelProperty(value = "BOM编码")
    private String code;
    /**
     * BOM名称
     */
    @ApiModelProperty(value = "BOM名称")
    private String name;
}
