package com.maxinhai.platform.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @ClassName：Algorithm
 * @Author: XinHai.Ma
 * @Date: 2026/1/13 22:41
 * @Description: 算法
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Algorithm {

    /**
     * 主键ID
     */
    private String id = UUID.randomUUID().toString().replaceAll("-", "");
    /**
     * 算法名称
     */
    private String name;
    /**
     * 算法类型ID
     */
    private String algorithmTypeId;

}
