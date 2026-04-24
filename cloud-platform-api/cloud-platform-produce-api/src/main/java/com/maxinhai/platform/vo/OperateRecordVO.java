package com.maxinhai.platform.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maxinhai.platform.enums.OperateType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "派工单操作记录VO")
public class OperateRecordVO {

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    protected String id;
    /**
     * 订单编码
     */
    @ApiModelProperty(value = "订单编码")
    private String orderCode;
    /**
     * 工单编码
     */
    @ApiModelProperty(value = "工单编码")
    private String workOrderCode;
    /**
     * 派工单ID
     */
    @ApiModelProperty(value = "派工单ID")
    private String taskOrderId;
    /**
     * 派工单编码
     */
    @ApiModelProperty(value = "派工单编码")
    private String taskOrderCode;
    /**
     * 产品ID
     */
    @ApiModelProperty(value = "产品ID")
    private String productId;
    @ApiModelProperty(value = "产品编码")
    private String productCode;
    @ApiModelProperty(value = "产品名称")
    private String productName;
    /**
     * BOM ID
     */
    @ApiModelProperty(value = "物料清单ID")
    private String bomId;
    @ApiModelProperty(value = "物料清单编码")
    private String bomCode;
    @ApiModelProperty(value = "物料清单名称")
    private String bomName;

    /**
     * 工艺路线ID
     */
    @ApiModelProperty(value = "工艺路线ID")
    private String routingId;
    @ApiModelProperty(value = "工艺路线编码")
    private String routingCode;
    @ApiModelProperty(value = "工艺路线名称")
    private String routingName;
    /**
     * 工序ID
     */
    @ApiModelProperty(value = "工序ID")
    private String operationId;
    @ApiModelProperty(value = "工序编码")
    private String operationCode;
    @ApiModelProperty(value = "工序名称")
    private String operationName;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private String operator;
    /**
     * 操作类型
     */
    @ApiModelProperty(value = "操作类型")
    private OperateType operateType;
    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date operateTime;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    protected String createBy;

    /**
     * 创建人用户名
     */
    @ApiModelProperty(value = "创建人用户名")
    private String creator;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date createTime;

}
