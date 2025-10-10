package com.maxinhai.platform.vo.model;

import com.google.common.collect.Lists;
import com.maxinhai.platform.po.INode;
import com.maxinhai.platform.po.model.Warehouse;
import com.maxinhai.platform.po.model.WarehouseArea;
import com.maxinhai.platform.po.model.WarehouseLocation;
import com.maxinhai.platform.po.model.WarehouseRack;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @ClassName：WarehouseTreeVO
 * @Author: XinHai.Ma
 * @Date: 2025/9/22 15:10
 * @Description: 仓库树状结构VO
 */
@Data
@ApiModel(description = "仓库树状结构VO")
public class WarehouseTreeVO implements INode {

    @ApiModelProperty(value = "主键ID")
    private String id;
    @ApiModelProperty(value = "仓库名称")
    private String name;
    @ApiModelProperty(value = "父级ID")
    private String parentId;
    @ApiModelProperty(value = "子项集合")
    private List<WarehouseTreeVO> children;

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
        this.children = (List<WarehouseTreeVO>) children;
    }

    @Override
    public List<? extends INode> getChildren() {
        return this.children;
    }

    public static WarehouseTreeVO convert(Warehouse warehouse) {
        WarehouseTreeVO vo = new WarehouseTreeVO();
        vo.setId(warehouse.getId());
        vo.setName(warehouse.getName());
        vo.setParentId("0");
        vo.setChildren(Lists.newArrayList());
        return vo;
    }

    public static WarehouseTreeVO convert(WarehouseArea area) {
        WarehouseTreeVO vo = new WarehouseTreeVO();
        vo.setId(area.getId());
        vo.setName(area.getName());
        vo.setParentId(area.getWarehouseId());
        vo.setChildren(Lists.newArrayList());
        return vo;
    }

    public static WarehouseTreeVO convert(WarehouseRack rack) {
        WarehouseTreeVO vo = new WarehouseTreeVO();
        vo.setId(rack.getId());
        vo.setName(rack.getName());
        vo.setParentId(rack.getAreaId());
        vo.setChildren(Lists.newArrayList());
        return vo;
    }

    public static WarehouseTreeVO convert(WarehouseLocation location) {
        WarehouseTreeVO vo = new WarehouseTreeVO();
        vo.setId(location.getId());
        vo.setName(location.getName());
        vo.setParentId(location.getRackId());
        vo.setChildren(Lists.newArrayList());
        return vo;
    }
}
