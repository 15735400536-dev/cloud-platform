package com.maxinhai.platform.utils;

import com.maxinhai.platform.po.INode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 树节点工具类，用于将实现INode接口的列表转换为树状结构
 */
public class TreeNodeUtils {

    /**
     * 将节点列表构建为树状结构
     *
     * @param nodes        节点列表
     * @param rootParentId 根节点的父ID（通常为null或空字符串）
     * @param <T>          实现INode接口的类型
     * @return 树状结构的根节点列表
     */
    public static <T extends INode> List<T> buildTree(List<T> nodes, String rootParentId) {
        if (nodes == null || nodes.isEmpty()) {
            return new ArrayList<>();
        }

        // 构建节点ID到节点的映射，便于快速查找
        Map<String, T> nodeMap = new HashMap<>();
        for (T node : nodes) {
            nodeMap.put(node.getId(), node);
        }

        // 存储根节点
        List<T> rootNodes = new ArrayList<>();

        // 遍历所有节点，建立父子关系
        for (T node : nodes) {
            String parentId = node.getParentId();

            // 判断是否为根节点
            if (rootParentId == null ? parentId == null : rootParentId.equals(parentId)) {
                rootNodes.add(node);
            } else {
                // 查找父节点并添加到其子节点列表
                T parentNode = nodeMap.get(parentId);
                if (parentNode != null) {
                    // 获取父节点当前的子节点列表
                    List<T> children = new ArrayList<>();
                    if (parentNode.getChildren() != null) {
                        for (INode child : parentNode.getChildren()) {
                            // 强制类型转换（因为我们确保了类型一致性）
                            children.add((T) child);
                        }
                    }
                    // 添加当前节点作为子节点
                    children.add(node);
                    // 更新父节点的子节点列表
                    parentNode.setChildren(children);
                }
            }
        }

        return rootNodes;
    }

    /**
     * 将节点列表构建为树状结构，默认根节点的父ID为null
     *
     * @param nodes 节点列表
     * @param <T>   实现INode接口的类型
     * @return 树状结构的根节点列表
     */
    public static <T extends INode> List<T> buildTree(List<T> nodes) {
        return buildTree(nodes, null);
    }

    /**
     * 从树状结构中扁平化为列表
     *
     * @param rootNodes 树的根节点列表
     * @param <T>       实现INode接口的类型
     * @return 扁平化后的节点列表
     */
    public static <T extends INode> List<T> flattenTree(List<T> rootNodes) {
        List<T> result = new ArrayList<>();
        if (rootNodes == null || rootNodes.isEmpty()) {
            return result;
        }

        for (T node : rootNodes) {
            // 添加当前节点
            result.add(node);
            // 递归添加子节点
            Iterable<? extends INode> children = node.getChildren();
            if (children != null) {
                List<T> childList = new ArrayList<>();
                for (INode child : children) {
                    childList.add((T) child);
                }
                result.addAll(flattenTree(childList));
            }
        }

        return result;
    }

}
