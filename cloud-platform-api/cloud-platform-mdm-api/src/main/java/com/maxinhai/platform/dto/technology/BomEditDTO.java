package com.maxinhai.platform.dto.technology;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "数据字典新增DTO")
public class BomEditDTO {

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
    /**
     * 版本号
     */
    @ApiModelProperty(value = "版本号")
    private String version;

}
