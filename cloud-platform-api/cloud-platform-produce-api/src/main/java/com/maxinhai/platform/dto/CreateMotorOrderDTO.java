package com.maxinhai.platform.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "创建电机订单DTO")
public class CreateMotorOrderDTO {

    @ApiModelProperty(value = "产品编码")
    String productCode;
    @ApiModelProperty(value = "订单数量")
    Integer orderCount;
    @ApiModelProperty(value = "计划开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date planBeginTime;
    @ApiModelProperty(value = "计划结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date planEndTime;
    @ApiModelProperty(value = "工艺路线编码")
    String routingCode;
    @ApiModelProperty(value = "工艺路线版本号")
    String routingVersion;
    @ApiModelProperty(value = "BOM编码")
    String bomCode;
    @ApiModelProperty(value = "BOM版本号")
    String bomVersion;

}
