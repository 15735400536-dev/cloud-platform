package com.maxinhai.platform.vo;

import com.google.common.collect.Lists;
import com.maxinhai.platform.po.INode;
import com.maxinhai.platform.po.MaterialType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "物料类型VO")
public class MaterialTypeTreeVO implements INode {

    @ApiModelProperty(value = "主键ID")
    private String id;
    @ApiModelProperty(value = "物料类型名称")
    private String name;
    @ApiModelProperty(value = "父级ID")
    private String parentId;
    @ApiModelProperty(value = "子项集合")
    private List<MaterialTypeTreeVO> children;

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public String getParentId() {
        return this.parentId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setChildren(List<? extends INode> children) {
        this.children = (List<MaterialTypeTreeVO>) children;
    }

    @Override
    public List<? extends INode> getChildren() {
        return this.children;
    }

    public static MaterialTypeTreeVO convert(MaterialType type) {
        MaterialTypeTreeVO vo = new MaterialTypeTreeVO();
        vo.setId(type.getId());
        vo.setName(type.getName());
        vo.setParentId(type.getParentId());
        vo.setChildren(Lists.newArrayList());
        return vo;
    }
}
