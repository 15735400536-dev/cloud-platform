package com.maxinhai.platform.vo;

import com.google.common.collect.Lists;

import com.maxinhai.platform.po.Dept;
import com.maxinhai.platform.po.INode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName：DeptTreeVO
 * @Author: XinHai.Ma
 * @Date: 2025/9/28 15:04
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
public class DeptTreeVO implements INode {

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
     * 子节点列表
     */
    private List<DeptTreeVO> children;

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
        this.children = (List<DeptTreeVO>) children;
    }

    @Override
    public List<? extends INode> getChildren() {
        return this.children;
    }

    public static DeptTreeVO convert(Dept dept) {
        DeptTreeVO treeVO = new DeptTreeVO();
        treeVO.setChildren(Lists.newArrayList());
        treeVO.setId(dept.getId());
        treeVO.setCode(dept.getCode());
        treeVO.setName(dept.getName());
        treeVO.setParentId(dept.getParentId());
        return treeVO;
    }
}
