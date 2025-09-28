package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName：Dept
 * @Author: XinHai.Ma
 * @Date: 2025/9/28 14:31
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_dept")
public class Dept extends RecordEntity {

    /**
     * 部门编码
     */
    private String code;
    /**
     * 部门名称
     */
    private String name;
    /**
     * 父部门ID（自关联字段）
     */
    private String parentId;
    /**
     * 部门负责人ID，关联用户表
     */
    private String leaderId;
    /**
     * 部门状态：1-启用，0-禁用；
     */
    private Integer status;
    /**
     * 排序号，用于部门列表展示时的顺序调整（数字越小越靠前）
     */
    private Integer sort;
    /**
     * 部门描述（如 “负责公司人才招聘与薪酬管理”），非必填
     */
    private String description;

}
