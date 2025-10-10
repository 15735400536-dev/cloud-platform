package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.InspectionConfigVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "巡检配置分页查询DTO")
public class InspectionConfigQueryDTO extends PageSearch<InspectionConfigVO> {

    /**
     * 配置编码
     */
    @ApiModelProperty("配置编码")
    private String configCode;
    /**
     * 配置名称
     */
    @ApiModelProperty("配置名称")
    private String configName;

}
