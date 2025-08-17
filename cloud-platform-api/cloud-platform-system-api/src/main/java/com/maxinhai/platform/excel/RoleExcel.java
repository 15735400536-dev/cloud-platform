package com.maxinhai.platform.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class RoleExcel {

    @ExcelProperty("角色标识")
    private String roleKey;
    @ExcelProperty("角色名称")
    private String roleName;
    @ExcelProperty("角色描述")
    private String roleDesc;

}
