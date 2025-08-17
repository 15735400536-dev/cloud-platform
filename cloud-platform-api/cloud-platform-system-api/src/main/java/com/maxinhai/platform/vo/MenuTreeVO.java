package com.maxinhai.platform.vo;

import com.maxinhai.platform.po.INode;
import com.maxinhai.platform.po.Menu;
import lombok.Data;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

@Data
public class MenuTreeVO implements INode {

    /**
     * 主键ID
     */
    private String id;
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
    /**
     * 子节点列表
     */
    private List<MenuTreeVO> children;

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getParentId() {
        return this.parentId;
    }

    @Override
    public void setChildren(List<? extends INode> children) {
        this.children = (List<MenuTreeVO>) children;
    }

    @Override
    public List<? extends INode> getChildren() {
        return this.children;
    }

    public static MenuTreeVO convert(Menu menu) {
        MenuTreeVO vo = new MenuTreeVO();
        vo.setId(menu.getId());
        vo.setParentId(menu.getParentId());
        vo.setMenuName(menu.getMenuName());
        vo.setUrl(menu.getUrl());
        vo.setComponent(menu.getComponent());
        vo.setIcon(menu.getIcon());
        vo.setMenuType(menu.getMenuType());
        vo.setSort(menu.getSort());
        vo.setStatus(menu.getStatus());
        vo.setIsFrame(menu.getIsFrame());
        vo.setChildren(Lists.newArrayList());
        return vo;
    }

}
