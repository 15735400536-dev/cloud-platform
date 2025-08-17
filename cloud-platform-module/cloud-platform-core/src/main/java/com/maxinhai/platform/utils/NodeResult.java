package com.maxinhai.platform.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NodeResult<T> {

    /**
     * 父级ID
     */
    private String parentId;
    /**
     * 名称
     */
    private String name;
    /**
     * 子项集合
     */
    private List<NodeResult<T>> children;

    /**
     * Bean转换树状结构
     * @param parentId 父级ID
     * @param name 名称
     * @param children 子项集合
     * @return
     * @param <T>
     */
    public static <T> NodeResult<T> convert(String parentId, String name, List<T> children) {
        NodeResult<T> nodeResult = new NodeResult<>();
        nodeResult.setParentId(parentId);
        nodeResult.setName(name);
        nodeResult.setChildren(null);
        return nodeResult;
    }

}
