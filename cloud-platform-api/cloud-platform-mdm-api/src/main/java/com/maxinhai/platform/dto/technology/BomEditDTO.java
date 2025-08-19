package com.maxinhai.platform.dto.technology;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("数据字典新增DTO")
public class BomEditDTO {

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
    /**
     * 版本号
     */
    @ApiModelProperty("版本号")
    private String version;

}
