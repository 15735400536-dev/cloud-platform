package com.maxinhai.platform.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

/**
 * @ClassName：IdEntity
 * @Author: XinHai.Ma
 * @Date: 2025/9/3 13:21
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class IdEntity {

    /**
     * 主键ID，使用MongoDB的ObjectId
     */
    @Id
    private String id;

}
