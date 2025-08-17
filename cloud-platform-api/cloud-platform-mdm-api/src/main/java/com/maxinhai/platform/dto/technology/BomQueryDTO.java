package com.maxinhai.platform.dto.technology;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.technology.BomVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("数据字典新增DTO")
public class BomQueryDTO extends PageSearch<BomVO> {

    /**
     * BOM编码
     */
    @ApiModelProperty("BOM编码")
    private String code;
    /**
     * BOM名称
     */
    @ApiModelProperty("BOM名称")
    private String name;
}
