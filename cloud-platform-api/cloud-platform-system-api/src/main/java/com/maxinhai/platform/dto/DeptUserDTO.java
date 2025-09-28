package com.maxinhai.platform.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName：DeptUserDTO
 * @Author: XinHai.Ma
 * @Date: 2025/9/28 14:41
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
@ApiModel(description = "部门关联用户")
public class DeptUserDTO {

    @ApiModelProperty("部门ID")
    private String deptId;
    @ApiModelProperty("用户ID集合")
    private List<String> userId;

}
