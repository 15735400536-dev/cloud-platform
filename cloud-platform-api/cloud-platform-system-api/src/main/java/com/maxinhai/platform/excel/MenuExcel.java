package com.maxinhai.platform.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class MenuExcel {

    @ExcelProperty("主键ID")
    private String id;
    /**
     * 父菜单ID（顶层菜单为0）
     */
    @ExcelProperty("父菜单ID")
    private String parentId;
    /**
     * 菜单名称
     */
    @ExcelProperty("菜单名称")
    private String menuName;
    /**
     * 菜单链接（如：/system/menu）
     */
    @ExcelProperty("菜单链接")
    private String url;
    /**
     * 前端组件路径（如：system/menu/index）
     */
    @ExcelProperty("前端组件路径")
    private String component;
    /**
     * 菜单图标（如：#icon-menu）
     */
    @ExcelProperty("菜单图标")
    private String icon;
    /**
     * 菜单类型（C:目录，M:菜单，B:按钮）
     */
    @ExcelProperty("菜单类型（C:目录，M:菜单，B:按钮）")
    private String menuType;
    /**
     * 排序值（越小越靠前）
     */
    @ExcelProperty("排序值（越小越靠前）")
    private Integer sort;
    /**
     * 状态（1:启用，0:禁用）
     */
    @ExcelProperty("状态（1:启用，0:禁用）")
    private Integer status;
    /**
     * 是否外链（1:是，0:否）
     */
    @ExcelProperty("是否外链（1:是，0:否）")
    private Integer isFrame;

}
