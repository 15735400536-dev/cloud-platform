package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.DeptVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @ClassName：DeptQueryDTO
 * @Author: XinHai.Ma
 * @Date: 2025/9/28 14:41
 * @Description: 部门分页查询DTO
 */
@Data
@ApiModel(description = "部门分页查询DTO")
public class DeptQueryDTO extends PageSearch<DeptVO> {

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

}
