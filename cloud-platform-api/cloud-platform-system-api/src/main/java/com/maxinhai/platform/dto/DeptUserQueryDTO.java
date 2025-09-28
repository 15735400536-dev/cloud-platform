package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.UserVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName：DeptUserQueryDTO
 * @Author: XinHai.Ma
 * @Date: 2025/9/28 15:39
 * @Description: 部门用户分页查询DTO
 */
@Data
@ApiModel(description = "部门用户分页查询DTO")
public class DeptUserQueryDTO extends PageSearch<UserVO> {

    @ApiModelProperty("部门ID")
    private String deptId;

}
