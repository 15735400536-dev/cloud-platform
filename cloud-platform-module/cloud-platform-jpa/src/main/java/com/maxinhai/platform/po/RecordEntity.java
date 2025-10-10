package com.maxinhai.platform.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import java.time.LocalDateTime;

/**
 * @ClassName：RecordEntity
 * @Author: XinHai.Ma
 * @Date: 2025/9/1 16:02
 * @Description: 记录基类
 */
@Data
@EntityListeners(AuditingEntityListener.class)
public abstract class RecordEntity extends IdEntity {

    /**
     * 审核状态
     */
    @Column(name = "del_flag", length = 3)
    private Integer delFlag;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @CreatedDate
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @CreatedBy
    @Column(name = "create_by", length = 32)
    private String createBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @LastModifiedDate
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 更新人
     */
    @LastModifiedBy
    @Column(name = "update_by", length = 32)
    private String updateBy;

}
