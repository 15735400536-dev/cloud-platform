package com.maxinhai.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "新增DTO")
public class MaintenanceConfigAddDTO {

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
    /**
     * 设备ID
     */
    @ApiModelProperty("设备ID")
    private String equipId;
    /**
     * 保养类型: 预防性保养 定期保养 换季保养 专项保养
     */
    @ApiModelProperty("保养类型: 预防性保养 定期保养 换季保养 专项保养")
    private String maintenanceType;
    /**
     * 保养等级: A级 B级 C级
     */
    @ApiModelProperty("保养等级: A级 B级 C级")
    private Integer maintenanceLevel;

    /**
     * 周期类型: 日 周 月 季度 年
     */
    @ApiModelProperty("周期类型: 日 周 月 季度 年")
    private Integer cycleType;
    /**
     * 周期间隔
     */
    @ApiModelProperty("周期间隔")
    private Integer cycleInterval;
    /**
     * 状态: 1.启用 0.禁用
     */
    @ApiModelProperty("状态: 1.启用 0.禁用")
    private Integer status;

}
