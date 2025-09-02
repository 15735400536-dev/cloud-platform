package com.maxinhai.platform.po;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @ClassName：RecordEntity
 * @Author: XinHai.Ma
 * @Date: 2025/9/1 16:02
 * @Description: 必须描述类做什么事情, 实现什么功能
 */
@Data
public class RecordEntity {

    /**
     * 审核状态
     */
    @Column(nullable = false)
    private Integer delFlag;

    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @Column(updatable = false)
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;

}
