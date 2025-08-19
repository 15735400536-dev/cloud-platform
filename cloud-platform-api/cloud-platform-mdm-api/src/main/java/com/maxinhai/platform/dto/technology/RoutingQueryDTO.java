package com.maxinhai.platform.dto.technology;

import com.maxinhai.platform.dto.PageSearch;
import com.maxinhai.platform.vo.technology.RoutingVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("数据字典新增DTO")
public class RoutingQueryDTO extends PageSearch<RoutingVO> {

    /**
     * 工艺路线编码
     */
    @ApiModelProperty("工艺路线编码")
    private String code;
    /**
     * 工艺路线名称
     */
    @ApiModelProperty("工艺路线名称")
    private String name;

}
