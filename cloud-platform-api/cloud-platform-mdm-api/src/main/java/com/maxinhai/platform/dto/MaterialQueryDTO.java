package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.MaterialVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "物料分页查询DTO")
public class MaterialQueryDTO extends PageSearch<MaterialVO> {

    @ApiModelProperty(value = "物料编码")
    private String code;
    @ApiModelProperty(value = "物料名称")
    private String name;

}
