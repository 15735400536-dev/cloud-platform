package com.maxinhai.platform.po;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 新闻审核记录表（news_audit_log）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "news_audit_log")
public class NewsAuditLog extends RecordEntity {

    /**
     * 新闻 ID
     */
    @Column(name = "news_id", nullable = false)
    private Long newsId;
    /**
     * 审核人 ID（关联 sys_user）
     */
    @Column(name = "auditor_id", nullable = false, length = 32)
    private String auditorId;
    /**
     * 审核意见（驳回时必填）
     */
    private String auditOpinion;
    /**
     * 审核结果（1 - 通过，2 - 驳回，3 - 打回修改）
     */
    private Integer auditStatus;
    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

}
