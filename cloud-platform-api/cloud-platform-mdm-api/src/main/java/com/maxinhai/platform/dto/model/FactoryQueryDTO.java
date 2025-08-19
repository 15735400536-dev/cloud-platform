package com.maxinhai.platform.dto.model;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.model.FactoryVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("工厂分页查询DTO")
public class FactoryQueryDTO extends PageSearch<FactoryVO> {

    @ApiModelProperty("工厂编码")
    private String code;
    @ApiModelProperty("工厂名称")
    private String name;

}
