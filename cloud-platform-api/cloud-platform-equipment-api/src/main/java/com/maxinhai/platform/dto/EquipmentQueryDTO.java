package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.EquipmentVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "设备分页查询VO")
public class EquipmentQueryDTO extends PageSearch<EquipmentVO> {

    /**
     * 设备编码
     */
    @ApiModelProperty("设备编码")
    private String equipCode;
    /**
     * 设备名称
     */
    @ApiModelProperty("设备名称")
    private String equipName;
    /**
     * 设备类型
     */
    @ApiModelProperty("设备类型")
    private String equipType;

}
