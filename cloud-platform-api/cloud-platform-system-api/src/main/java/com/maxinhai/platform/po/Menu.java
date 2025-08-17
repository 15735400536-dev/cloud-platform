package com.maxinhai.platform.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName("sys_menu")
public class Menu extends RecordEntity {

    /**
     * 父菜单ID（顶层菜单为0）
     */
    private String parentId;
    /**
     * 菜单名称
     */
    private String menuName;
    /**
     * 菜单链接（如：/system/menu）
     */
    private String url;
    /**
     * 前端组件路径（如：system/menu/index）
     */
    private String component;
    /**
     * 菜单图标（如：#icon-menu）
     */
    private String icon;
    /**
     * 菜单类型（C:目录，M:菜单，B:按钮）
     */
    private String menuType;
    /**
     * 排序值（越小越靠前）
     */
    private Integer sort;
    /**
     * 状态（1:启用，0:禁用）
     */
    private Integer status;
    /**
     * 是否外链（1:是，0:否）
     */
    private Integer isFrame;
}
