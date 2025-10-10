package com.maxinhai.platform.po;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 新闻评论表（news_comment）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "news_comment")
public class NewsComment extends RecordEntity {

    /**
     * 新闻 ID
     */
    @Column(name = "news_id", nullable = false)
    private Long newsId;
    /**
     * 评论用户 ID（关联 sys_user，游客为 0）
     */
    @Column(name = "user_id", nullable = false, length = 32)
    private String userId;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 父评论 ID（0 - 一级评论，用于回复）
     */
    @Column(name = "parent_id", nullable = false)
    private Long parentId;
    /**
     * 状态（0 - 待审核，1 - 正常，2 - 违规删除）
     */
    @Column(name = "status", nullable = false)
    private Integer status;

}
