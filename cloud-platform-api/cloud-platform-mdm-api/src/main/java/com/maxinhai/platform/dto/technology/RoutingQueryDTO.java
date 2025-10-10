package com.maxinhai.platform.dto.technology;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.technology.RoutingVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "工艺路线分页查询DTO")
public class RoutingQueryDTO extends PageSearch<RoutingVO> {

    /**
     * 工艺路线编码
     */
    @ApiModelProperty(value = "工艺路线编码")
    private String code;
    /**
     * 工艺路线名称
     */
    @ApiModelProperty(value = "工艺路线名称")
    private String name;

}
