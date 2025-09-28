package com.maxinhai.platform.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName：DeptVO
 * @Author: XinHai.Ma
 * @Date: 2025/9/28 14:42
 * @Description: 部门VO
 */
@Data
@ApiModel(description = "部门VO")
public class DeptVO {

    @ApiModelProperty("主键ID")
    private String id;
    /**
     * 部门编码
     */
    @ApiModelProperty("部门编码")
    private String code;
    /**
     * 部门名称
     */
    @ApiModelProperty("部门名称")
    private String name;
    /**
     * 父级部门ID（自关联字段）
     */
    @ApiModelProperty("父级部门ID")
    private String parentId;
    /**
     * 部门负责人ID，关联用户表
     */
    @ApiModelProperty("部门负责人ID")
    private String leaderId;
    @ApiModelProperty("部门负责人名称")
    private String leaderName;
    /**
     * 部门状态：1-启用，0-禁用
     */
    @ApiModelProperty("部门状态：1-启用，0-禁用")
    private Integer status;
    /**
     * 排序号，用于部门列表展示时的顺序调整（数字越小越靠前）
     */
    @ApiModelProperty("排序号，用于部门列表展示时的顺序调整（数字越小越靠前）")
    private Integer sort;
    /**
     * 部门描述（如 “负责公司人才招聘与薪酬管理”），非必填
     */
    @ApiModelProperty("部门描述（如 “负责公司人才招聘与薪酬管理”），非必填")
    private String description;

}
