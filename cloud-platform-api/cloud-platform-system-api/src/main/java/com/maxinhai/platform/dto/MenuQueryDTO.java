package com.maxinhai.platform.dto;

import com.maxinhai.platform.vo.MenuVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("菜单分页查询DTO")
public class MenuQueryDTO extends PageSearch<MenuVO> {

    @ApiModelProperty("菜单名称")
    private String menuName;

}
