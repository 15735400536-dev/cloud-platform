package com.maxinhai.platform.po;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * JPA实体通用基类
 * 所有实体类可继承此类，减少重复代码
 */
@Data
@MappedSuperclass // 标识为父类，不会生成单独的表
@EntityListeners(AuditingEntityListener.class) // 启用实体监听器，用于触发审计逻辑
public abstract class RecordEntity implements Serializable {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增主键
    private Long id;
    /**
     * 逻辑删除标识: 0.未删除 1.已删除
     */
    @Column(name = "del_flag", nullable = false)
    private Integer delFlag = 0;
    /**
     * 创建人
     */
    @Column(name = "create_by", nullable = false, length = 32)
    private String createBy = "anonymous";
    /**
     * 创建时间
     */
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime = LocalDateTime.now();
    /**
     * 更新人
     */
    @Column(name = "update_by", nullable = false, length = 32)
    private String updateBy = "anonymous";
    /**
     * 更新时间
     */
    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime = LocalDateTime.now();

}
