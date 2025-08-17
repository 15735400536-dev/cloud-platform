package com.maxinhai.platform.po;

import java.util.List;

/**
 * 树节点接口，所有需要构建树状结构的类都需实现此接口
 */
public interface INode {

    /**
     * 获取节点ID
     */
    String getId();

    /**
     * 获取父节点ID
     */
    String getParentId();

    /**
     * 设置子节点列表
     */
    void setChildren(List<? extends INode> children);

    /**
     * 获取子节点列表
     */
    List<? extends INode> getChildren();

}
